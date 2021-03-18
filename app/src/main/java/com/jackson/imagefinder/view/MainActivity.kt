package com.jackson.imagefinder.view

import android.os.Bundle
import com.jackson.imagefinder.BaseActivity
import com.jackson.imagefinder.R
import com.jackson.imagefinder.view.ui.MainUI
import com.jackson.imagefinder.viewModel.ImageViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity: BaseActivity<MainUI, ImageViewModel>() {


    override fun onCreatedView(): MainUI? = MainUI()
    override fun onCreatedViewModel(): ImageViewModel = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.stay, R.anim.stay)
        
        viewModel?.searchRepositories(query = "안드로이드", sort = "accuracy", page = 1, pageSize = 3, isFirst= true)

        // viewModel = getViewModel()
//        viewModel.searchRepositories(query = "안드로이드", sort = "accuracy", page = 1, pageSize = 3)
//
//        viewModel.items.observe(this, Observer<List<ImageData>> {
//            println("test: ${it.size}")
//        })

        // viewModel.items.set(arrayListOf<ImageData>().apply { add(ImageData()) })
        // var temp: MetaData? = viewModel.meta.get()


//        // var service: ImageViewModel = get<ImageViewModel>().create(ImageViewModel::class.java)
//        CoroutineScope(Dispatchers.IO).launch {
//            //var service: ImageViewModel = getViewModel()
//            //service.searchRepositories()
//
//            viewModel.searchRepositories(query = "mvvm", page = 1, pageSize = 3)
//        }

    }
}