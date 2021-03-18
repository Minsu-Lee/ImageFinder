package com.jackson.imagefinder.base

import android.app.Activity
import org.jetbrains.anko.AnkoComponent

open abstract class BaseView: AnkoComponent<Activity> {
    var vm: BaseViewModel? = null
}