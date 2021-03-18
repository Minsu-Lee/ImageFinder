package com.jackson.imagefinder.view.adapter.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.view.adapter.ui.ImageGuideUI
import org.jetbrains.anko.AnkoContext

class ImageGuideViewHolder(parent: ViewGroup, ui: ImageGuideUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent)))