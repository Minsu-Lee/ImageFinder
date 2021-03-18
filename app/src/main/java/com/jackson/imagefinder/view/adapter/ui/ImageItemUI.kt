package com.jackson.imagefinder.view.adapter.ui

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.jackson.imagefinder.R
import com.jackson.imagefinder.extensions.squareImageView
import org.jetbrains.anko.*

class ImageItemUI: AnkoComponent<ViewGroup> {

    lateinit var imageCard: RelativeLayout

    lateinit var imageIv: ImageView

    lateinit var imageCategoryTv: TextView

    lateinit var imageSiteTv: TextView

    lateinit var imageDateTv: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(width= matchParent, height= wrapContent)

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL

                imageCard = relativeLayout {
                    padding = dip(7)

                    /** image */
                    imageIv = squareImageView {
                        id = R.id.search_image_thumb_left
                        scaleType = ImageView.ScaleType.FIT_XY
                    }.lparams(width= matchParent, height= matchParent)

                    /** category */
                    linearLayout {

                        backgroundResource = R.drawable.category_bg
                        gravity = Gravity.CENTER

                        imageCategoryTv = textView {
                            includeFontPadding = false

                            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.search_image_collection))
                            textColor = Color.WHITE
                            typeface = ResourcesCompat.getFont(context, R.font.barlow_medium)
                            gravity = Gravity.CENTER_VERTICAL

                        }.lparams(width= wrapContent, height= wrapContent) {
                            topMargin = dip(1)
                            bottomMargin = dip(2)
                            horizontalMargin = dip(4)
                        }

                    }.lparams(width= wrapContent, height= wrapContent) {
                        sameTop(R.id.search_image_thumb_left)
                        sameLeft(R.id.search_image_thumb_left)
                    }


                    verticalLayout {
                        verticalPadding = dip(5)
                        backgroundColor = Color.WHITE
                        gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL

                        /** site_name */
                        imageSiteTv = textView {
                            includeFontPadding = false
                            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL

                            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.search_image_collection))
                            textColor = Color.parseColor("#363636")
                            typeface = ResourcesCompat.getFont(context, R.font.barlow_bold)
                            gravity = Gravity.CENTER_VERTICAL

                        }.lparams(width= wrapContent, height= wrapContent)

                        /** datetime */
                        imageDateTv = textView {
                            includeFontPadding = false
                            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL

                            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.search_image_collection))
                            textColor = Color.parseColor("#5a5a5a")
                            typeface = ResourcesCompat.getFont(context, R.font.barlow_medium)
                            gravity = Gravity.CENTER_VERTICAL

                        }.lparams(width= wrapContent, height= wrapContent)

                    }.lparams(width= matchParent, height= wrapContent) {
                        bottomOf(R.id.search_image_thumb_left)
                    }

                }.lparams(width= wrapContent, height = wrapContent)

            }.lparams(width= wrapContent, height= wrapContent)

        }
    }
}