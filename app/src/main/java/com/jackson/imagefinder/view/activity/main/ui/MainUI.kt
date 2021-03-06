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
                    // ????????? ????????????????????? ?????? ????????? ?????? LiveData??? ??????
                    setLiveData(vm?.lifecycleOwner, vm?.pQuery) { curText ->
                        vm?.run {

                            // ??? ?????????????????? ???????????? ?????? ????????? ?????? ??????
                            if (itemStatus.get() != ListItemStatus.FIRST)
                                itemStatus.set(ListItemStatus.EMPTY)

                            // ?????? ????????????, ????????? ??? page ?????????
                            pPage.set(AppConst.PAGE_FIRST_VALUE)
                            itemClear()


                            // 1??? ?????? ????????? ????????? ?????? ??????
                            if (curText.isNotEmpty()) {

                                disposable?.dispose()
                                disposable = Observable.timer(1, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { searchRepositories() }

                            } else {

                                disposable?.dispose()
                                disposable = Observable.timer(1, TimeUnit.SECONDS)
                                    // setValue() ?????? : https://class-programming.tistory.com/116
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe {
                                        // ??? ????????? ???????????? ??????
                                        itemStatus.set(ListItemStatus.FIRST)
                                        itemClear()
                                        keyboardStatus(false)
                                    }
                            }
                        }
                    }

                    // liveData??? ????????? ??????????????? ???????????? ?????? ??????
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

                /** ?????? ?????? */
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

                    setOnClickListener { vm?.onRefresh() }

                }.lparams(width= dip(60), height= dip(60)) {
                    centerVertically()
                    alignParentRight()
                }

            }.lparams(width= matchParent, height= wrapContent)

            refreshLayout = swipeRefreshLayout {

                // refreshListener ??????
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

                    // Adapter ?????????
                    adapter = initAdapter()

                    // ViewModel??? items??? ???????????????.
                    initItemSubscribe {

                        /**
                         * api??? ???????????? ????????? ??????????????? loading status??? ????????? ???????????? ????????? ????????????.
                         * items??? ????????? ???????????? API????????? ?????? ????????? ?????????????????? ??????,
                         * loading status??? true??? ????????? ??????.
                         */
                        if (mImageAdapter.beforeSize != it.size) scrollListener?.loadingStatus(true)

                        if (it.size == 0) clearScrollListener()
                        else if (it.size == AppConst.PER_PAGE_DEFAULT && scrollListener == null) {
                            initListScrollListener()
                        } else if (it.size == 0) scrollListener?.let { listener -> removeOnScrollListener(listener) }

                        // ????????? ??????
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
            EditorInfo.IME_ACTION_SEARCH -> vm?.onRefresh()
            else -> return false // ?????? ????????? ??????
        }
        return true
    }

    private fun initAdapter(): ImageListAdapter? = vm?.run {
        return ImageListAdapter(this).also { adapter ->
            mImageAdapter = adapter
        }
    }

    // ????????? items ?????? ???????????? callback?????? ??????
    private fun initItemSubscribe(itemCallback: (ArrayList<ImageData>)->Unit) = vm?.run {
        lifecycleOwner?.let { owner ->
            // items ?????? ??????
            items.observe(owner, Observer { itemCallback(it) })
        }
    }

    // scrollListener ?????????
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

    // scrollListener ??????
    private fun RecyclerView.clearScrollListener() {
        scrollListener?.let { listener -> removeOnScrollListener(listener) }
        scrollListener = null
    }

}