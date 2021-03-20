package com.jackson.imagefinder.viewModel

import android.content.Context
import com.bumptech.glide.Glide
import com.jackson.imagefinder.R
import com.jackson.imagefinder.base.BaseViewModel
import com.jackson.imagefinder.model.ImageData
import com.jackson.imagefinder.utils.DeviceUtils
import com.jackson.imagefinder.view.activity.imageDetail.ui.ImageDetailUI

class ImageDetailViewModel: BaseViewModel() {

    companion object {
        val TAG = javaClass.simpleName
    }

    lateinit var imageData: ImageData

    fun loadImageSrc(view: ImageDetailUI) = with(view) {
        Glide.with(iv.context)
            .load(imageData.image_url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.not_found_img)
            .into(iv)

        iv.minimumHeight = calcImgHeight(iv.context, imageData.width, imageData.height)
    }

    /**
     * 이미지 높이 계산
     * deviceWidth: imgHeight = width : height
     */
    private fun calcImgHeight(ctx: Context, width: Int, height: Int): Int {
        val deviceWidth = DeviceUtils.getDeviceSize(ctx).x
        return deviceWidth * height / width
    }

}