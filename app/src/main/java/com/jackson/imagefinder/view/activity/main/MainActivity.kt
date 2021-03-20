package com.jackson.imagefinder.view.activity.main

import android.os.Bundle
import com.jackson.imagefinder.BaseActivity
import com.jackson.imagefinder.R
import com.jackson.imagefinder.view.activity.main.ui.MainUI
import com.jackson.imagefinder.viewModel.ImageViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity: BaseActivity<MainUI, ImageViewModel>() {

    override var view: MainUI = MainUI()
    override fun onCreateViewModel(): ImageViewModel = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.stay, R.anim.stay)
    }

}