package com.jackson.imagefinder.view.activity.ui

import android.app.Activity
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.R
import com.jackson.imagefinder.base.BaseView
import com.jackson.imagefinder.base.WrapContentLayoutManager
import com.jackson.imagefinder.extensions.getDefault
import com.jackson.imagefinder.viewModel.ImageViewModel
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MainUI: BaseView() {

    lateinit var mSearchEt: EditText

    lateinit var mSearchBtn: LinearLayout

    lateinit var rv: RecyclerView

    override fun createView(ui: AnkoContext<Activity>) = with(ui) {
        verticalLayout {
            backgroundColor = Color.WHITE

            relativeLayout {
                backgroundColor = Color.TRANSPARENT

                mSearchEt = editText {
                    setText((vm as? ImageViewModel)?.pQuery?.getDefault("") ?: "")
                    textColor = Color.BLACK
                    imeOptions = EditorInfo.IME_ACTION_SEARCH
                    inputType = InputType.TYPE_CLASS_TEXT
                    gravity = Gravity.CENTER_VERTICAL
                    hint = ctx.getString(R.string.image_keyword_hint)
                    verticalPadding = 0
                    leftPadding = dip(12)
                    rightPadding = dip(60 + 12)
                }.lparams(width= matchParent, height= wrapContent) {
                    alignParentTop()
                    alignParentLeft()
                    sameBottom(R.id.image_search_btn)
                }

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

                    setOnClickListener { (vm as? ImageViewModel)?.searchRepositories() }
                }.lparams(width= dip(60), height= dip(60)) {
                    alignParentRight()
                    centerVertically()
                }

            }.lparams(width= matchParent, height= wrapContent)

            rv = recyclerView {
                backgroundColor = Color.WHITE
                layoutManager = WrapContentLayoutManager(ctx, RecyclerView.VERTICAL, false)
            }.lparams(width= matchParent, height=0, weight = 1f)

        }
    }

}