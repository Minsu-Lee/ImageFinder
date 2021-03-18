package com.jackson.imagefinder

import android.app.Application
import com.jackson.imagefinder.di.apiModule
import com.jackson.imagefinder.di.networkModule
import com.jackson.imagefinder.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)

            koin.loadModules(listOf(networkModule,
                apiModule,
                viewModelModule))
        }

    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }

}