package com.jackson.imagefinder.view.activity.imageDetail.ui

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import com.jackson.imagefinder.R
import com.jackson.imagefinder.base.BaseView
import com.jackson.imagefinder.viewModel.ImageDetailViewModel
import org.jetbrains.anko.*

class ImageDetailUI(var act: Activity): BaseView<ImageDetailViewModel>() {

    companion object {
        val TAG = ImageDetailUI::class.java.simpleName
    }

    lateinit var sv: ScrollView

    lateinit var iv: ImageView

    lateinit var closeIv: LinearLayout

    override fun createView(ui: AnkoContext<Activity>) = with(ui) {
        relativeLayout {
            backgroundColor = Color.BLACK
            lparams(width= matchParent, height= matchParent)

            sv = scrollView {
                isFillViewport = true

                verticalLayout {
                    gravity = Gravity.CENTER_VERTICAL

                    iv = imageView {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }.lparams(width= matchParent, height= wrapContent)

                }.lparams(width= matchParent, height= matchParent)

            }.lparams(width= matchParent, height= matchParent)

            closeIv = verticalLayout {
                gravity = Gravity.CENTER

                imageView(R.drawable.ic_detail_close) {
                    scaleType = ImageView.ScaleType.FIT_START
                }.lparams(width= dip(30), height= dip(30))

                setOnClickListener { act.onBackPressed() }

            }.lparams(width= dip(60), height= dip(60)) {
                alignParentLeft()
                alignParentTop()
            }

        }
    }

}