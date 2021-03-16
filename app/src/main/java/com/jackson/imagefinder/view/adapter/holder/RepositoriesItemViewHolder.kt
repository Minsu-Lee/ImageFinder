package com.jackson.imagefinder.view.adapter.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.view.adapter.ui.RepositoriesItemUI
import org.jetbrains.anko.AnkoContext

class RepositoriesItemViewHolder(parent: ViewGroup, val ui: RepositoriesItemUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent))) {

     fun onBind() = with(ui) {

     }
}