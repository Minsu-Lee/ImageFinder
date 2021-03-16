package com.jackson.imagefinder.view.adapter.ui

import android.graphics.Color
import android.view.ViewGroup
import org.jetbrains.anko.*

class EmptyItemUI: AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(width= matchParent, height= wrapContent) {
                backgroundColor = Color.TRANSPARENT
            }
        }
    }
}