package com.jackson.imagefinder.view.adapter.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.view.adapter.ui.ImageGuideUI
import com.jackson.imagefinder.view.adapter.ui.NotFoundGuideUI
import org.jetbrains.anko.AnkoContext

class NotFoundGuideViewHolder(parent: ViewGroup, ui: NotFoundGuideUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent)))