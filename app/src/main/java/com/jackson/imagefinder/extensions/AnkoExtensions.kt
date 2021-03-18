package com.jackson.imagefinder.extensions

import android.view.View

//inline fun ViewManager.flowLayout() = flowLayout {}
//inline fun ViewManager.flowLayout(init: FlowLayout.() -> Unit): FlowLayout {
//    return ankoView({ FlowLayout(it) }, theme = 0, init = init)
//}

fun View.string(res: Int): String = context.resources.getString(res)
fun View.integer(res: Int): Int = context.resources.getInteger(res)