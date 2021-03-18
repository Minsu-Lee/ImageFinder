package com.jackson.imagefinder.base

import android.app.Activity
import org.jetbrains.anko.AnkoComponent

open abstract class BaseView<VIEWMODEL: BaseViewModel>: AnkoComponent<Activity> {
    var vm: VIEWMODEL? = null
}