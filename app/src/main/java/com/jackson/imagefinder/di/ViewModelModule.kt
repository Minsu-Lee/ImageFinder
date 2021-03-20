package com.jackson.imagefinder.di

import com.jackson.imagefinder.viewModel.ImageDetailViewModel
import com.jackson.imagefinder.viewModel.ImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ImageViewModel(get()) }
    viewModel { ImageDetailViewModel() }
}