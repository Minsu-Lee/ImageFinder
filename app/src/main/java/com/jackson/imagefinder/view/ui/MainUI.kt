package com.jackson.imagefinder.view.ui

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.R
import com.jackson.imagefinder.base.AppConst
import com.jackson.imagefinder.base.BaseView
import com.jackson.imagefinder.base.ListItemStatus
import com.jackson.imagefinder.extensions.*
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

    lateinit var mSearchEt: EditText

    lateinit var mSearchBtn: LinearLayout

    lateinit var rv: RecyclerView

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
                            if (itemStatus.get() != ListItemStatus.FIRST)
                                itemStatus.set(ListItemStatus.EMPTY)

                            // 첫 페이지부터 호출
                            pPage.set(AppConst.PAGE_FIRST_VALUE)
                            itemClear()
                        }

                        // 1초 이내 변동이 없으면 검색 호출
                        if (curText.isNotEmpty()) {
                            disposable?.dispose()
                            disposable = Observable.timer(1, TimeUnit.SECONDS)
                                    // setValue() 참조 : https://class-programming.tistory.com/116
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe {
                                    vm?.searchRepositories()
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

}