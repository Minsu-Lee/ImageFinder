package com.jackson.imagefinder.di

import com.jackson.imagefinder.network.KakaoAPIService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { get<Retrofit>().create(KakaoAPIService::class.java) }
}