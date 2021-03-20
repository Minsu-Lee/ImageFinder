package com.jackson.imagefinder.view.activity.main.ui

import android.app.Activity
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jackson.imagefinder.R
import com.jackson.imagefinder.base.AppConst
import com.jackson.imagefinder.base.BaseView
import com.jackson.imagefinder.base.ListItemStatus
import com.jackson.imagefinder.base.ListMoreScrollListener
import com.jackson.imagefinder.extensions.*
import com.jackson.imagefinder.model.ImageData
import com.jackson.imagefinder.model.MetaData
import com.jackson.imagefinder.view.adapter.ImageListAdapter
import com.jackson.imagefinder.view.component.DefaultDividerItemDecoration
import com.jackson.imagefinder.viewModel.ImageViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.util.concurrent.TimeUnit

class MainUI : BaseView<ImageViewModel>(), TextView.OnEditorActionListener {

    companion object {
        val TAG = MainUI::class.java.simpleName
    }

    lateinit var mSearchEt: EditText

    lateinit var mSearchBtn: LinearLayout

    lateinit var refreshLayout: SwipeRefreshLayout

    lateinit var rv: RecyclerView

    lateinit var mImageAdapter: ImageListAdapter

    private var scrollListener: ListMoreScrollListener? = null

    override fun createView(ui: AnkoContext<Activity>) = with(ui) {
        verticalLayout {
            backgroundColor = Color.WHITE

            relativeLayout {
                backgroundColor = Color.TRANSPARENT

                mSearchEt = editText {

                    var disposable: Disposable? = null
                    // 양방향 데이터바인딩을 위해 아래와 같이 LiveData를 사용
                    setLiveData(vm?.lifecycleOwner, vm?.pQuery) { curText ->
                        vm?.run {

                            // 첫 가이드화면을 유지하기 위해 아래와 같이 처리
                            if (itemStatus.get() != ListItemStatus.FIRST)
                                itemStatus.set(ListItemStatus.EMPTY)

                            // 값이 변동되면, 리스트 및 page 초기화
                            pPage.set(AppConst.PAGE_FIRST_VALUE)
                            itemClear()


                            // 1초 이내 변동이 없으면 검색 호출
                            if (curText.isNotEmpty()) {

                                disposable?.dispose()
                                disposable = Observable.timer(1, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { searchRepositories() }

                            } else {

                                disposable?.dispose()
                                disposable = Observable.timer(1, TimeUnit.SECONDS)
                                    // setValue() 참조 : https://class-programming.tistory.com/116
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe {
                                        // 첫 가이드 화면으로 노출
                                        itemStatus.set(ListItemStatus.FIRST)
                                        itemClear()
                                        keyboardStatus(false)
                                    }
                            }
                        }
                    }

                    // liveData로 키보드 노출여부를 제어하기 위해 사용
                    initKeyboardControl(vm?.lifecycleOwner, vm?.keyboardStatus)

                    textColor = Color.BLACK
                    imeOptions = EditorInfo.IME_ACTION_SEARCH
                    inputType = InputType.TYPE_CLASS_TEXT
                    gravity = Gravity.CENTER_VERTICAL
                    hint = ctx.getString(R.string.image_keyword_hint)
                    verticalPadding = 0
                    leftPadding = dip(12)
                    rightPadding = dip(60 + 12)

                    setOnEditorActionListener(this@MainUI)

                }.lparams(width= matchParent, height= wrapContent) {
                    alignParentTop()
                    alignParentLeft()
                    sameBottom(R.id.image_search_btn)
                }

                /** 검색 버튼 */
                mSearchBtn = linearLayout {
                    id = R.id.image_search_btn
                    isClickable = true
                    gravity = Gravity.CENTER

                    imageView(R.drawable.ic_image_search) {
                        backgroundColor = Color.TRANSPARENT
                        adjustViewBounds = true
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        padding = 0
                    }.lparams(width= dip(30), height= dip(30))

                    setOnClickListener { vm?.searchRepositories() }

                }.lparams(width= dip(60), height= dip(60)) {
                    centerVertically()
                    alignParentRight()
                }

            }.lparams(width= matchParent, height= wrapContent)

            refreshLayout = swipeRefreshLayout {

                // refreshListener 추가
                vm?.let { setOnRefreshListener(it) }

                rv = recyclerView {
                    backgroundColor = Color.WHITE

                    addItemDecoration(DefaultDividerItemDecoration(ctx, DividerItemDecoration.VERTICAL, R.drawable.divider))
                    layoutManager = GridLayoutManager(ctx, 3).apply {
                        spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return vm?.itemStatus?.getDefault(ListItemStatus.FIRST)?.let {
                                    when(it) {
                                        ListItemStatus.FULL -> 1
                                        else -> 3
                                    }
                                } ?: 3
                            }
                        }
                    }

                    // Adapter 초기화
                    adapter = initAdapter()

                    // ViewModel의 items를 구독합니다.
                    initItemSubscribe {

                        /**
                         * api가 호출되면 응답을 받기전까지 loading status로 여러번 호출하지 않도록 막고있음.
                         * items에 변화가 있다는건 API호출로 부터 응답을 받았다는걸로 판단,
                         * loading status를 true로 초기화 한다.
                         */
                        if (mImageAdapter.beforeSize != it.size) scrollListener?.loadingStatus(true)

                        if (it.size == 0) clearScrollListener()
                        else if (it.size == AppConst.PER_PAGE_DEFAULT && scrollListener == null) {
                            initListScrollListener()
                        } else if (it.size == 0) scrollListener?.let { listener -> removeOnScrollListener(listener) }

                        // 리스트 갱신
                        with(mImageAdapter) {
                            if (beforeSize < it.size) {
                                beforeSize = it.size
                                notifyItemRangeInserted(beforeSize, it.size)
                            } else {
                                beforeSize = it.size
                                notifyDataSetChanged()
                            }
                        }

                        refreshLayout.isRefreshing = false
                    }

                }.lparams(width= matchParent, height= matchParent)

            }.lparams(width= matchParent, height=0, weight = 1f)
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> vm?.searchRepositories()
            else -> return false // 기본 엔터키 동작
        }
        return true
    }

    private fun initAdapter(): ImageListAdapter? = vm?.run {
        return ImageListAdapter(this).also { adapter ->
            mImageAdapter = adapter
        }
    }

    // 편의상 items 구독 데이터를 callback으로 반환
    private fun initItemSubscribe(itemCallback: (ArrayList<ImageData>)->Unit) = vm?.run {
        lifecycleOwner?.let { owner ->
            // items 변화 구독
            items.observe(owner, Observer { itemCallback(it) })
        }
    }

    // scrollListener 초기화
    private fun RecyclerView.initListScrollListener() {
        if (scrollListener == null) vm?.run {
            scrollListener = object: ListMoreScrollListener() {
                override fun getMetaInfo(): MetaData = meta.value ?: MetaData()
                override fun onLoadData(page: Int) {
                    pPage.value = page
                    searchRepositories()
                }
                override fun onCompletedLoading() {
                    scrollListener?.let { this@initListScrollListener.removeOnScrollListener(it) }
                }
            }.also { listener -> addOnScrollListener(listener) }
        }
    }

    // scrollListener 제거
    private fun RecyclerView.clearScrollListener() {
        scrollListener?.let { listener -> removeOnScrollListener(listener) }
        scrollListener = null
    }

}