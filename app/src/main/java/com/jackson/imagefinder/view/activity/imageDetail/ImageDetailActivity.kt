package com.jackson.imagefinder.view.activity.imageDetail

import android.os.Bundle
import com.jackson.imagefinder.BaseActivity
import com.jackson.imagefinder.R
import com.jackson.imagefinder.base.AppConst
import com.jackson.imagefinder.view.activity.imageDetail.ui.ImageDetailUI
import com.jackson.imagefinder.viewModel.ImageDetailViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ImageDetailActivity: BaseActivity<ImageDetailUI, ImageDetailViewModel>() {

    override var view: ImageDetailUI = ImageDetailUI(this@ImageDetailActivity)
    override fun onCreateViewModel(): ImageDetailViewModel = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            imageData = intent.getParcelableExtra(AppConst.IMAGE_DATA_NAME)
            loadImageSrc(view)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.act_slide_right_out)
    }

}