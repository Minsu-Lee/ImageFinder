package com.jackson.imagefinder.view.adapter.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.view.adapter.ui.EmptyItemUI
import org.jetbrains.anko.AnkoContext

class EmptyItemViewHolder(parent: ViewGroup, ui: EmptyItemUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent)))