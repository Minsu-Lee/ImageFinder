package com.jackson.imagefinder.view.adapter.ui

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.jackson.imagefinder.R
import com.jackson.imagefinder.utils.DeviceUtils
import org.jetbrains.anko.*

class ImageGuideUI: AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(width= matchParent, height= wrapContent) {
                backgroundColor = Color.WHITE

                relativeLayout {

                    var deviceHeight = DeviceUtils.getDeviceSize(context).y / 2 - dip(60)
                    textView(R.string.first_guide_str) {

                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.first_guide_size))
                        textColor = Color.parseColor("#0366d6")
                        typeface = ResourcesCompat.getFont(context, R.font.barlow_medium)
                        gravity = Gravity.CENTER

                    }.lparams(width= wrapContent, height= wrapContent) {
                        centerHorizontally()
                        topMargin = deviceHeight
                    }

                }.lparams(width= matchParent, height= matchParent)

            }
        }
    }
}