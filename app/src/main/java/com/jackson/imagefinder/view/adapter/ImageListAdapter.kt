package com.jackson.imagefinder.view.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jackson.imagefinder.base.ListItemStatus
import com.jackson.imagefinder.extensions.getDefault
import com.jackson.imagefinder.utils.DLog
import com.jackson.imagefinder.view.adapter.holder.EmptyItemViewHolder
import com.jackson.imagefinder.view.adapter.holder.ImageGuideViewHolder
import com.jackson.imagefinder.view.adapter.holder.ImageItemViewHolder
import com.jackson.imagefinder.view.adapter.holder.NotFoundGuideViewHolder
import com.jackson.imagefinder.view.adapter.ui.EmptyItemUI
import com.jackson.imagefinder.view.adapter.ui.ImageGuideUI
import com.jackson.imagefinder.view.adapter.ui.ImageItemUI
import com.jackson.imagefinder.view.adapter.ui.NotFoundGuideUI
import com.jackson.imagefinder.viewModel.ImageViewModel

class ImageListAdapter(owner: LifecycleOwner, var viewModel: ImageViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG = "ImageListAdapter"
        const val TYPE_FIRST_GUIDE_CARD = 0
        const val TYPE_ITEM_CARD = 100
        const val TYPE_NOT_FOUND_CARD = 200
        const val TYPE_EMPTY_CARD = 300
    }

    var beforeSize: Int = 0

    init {
        viewModel.items.observe(owner, Observer {
            if (beforeSize < it.size){
                beforeSize = it.size
                notifyItemRangeInserted(beforeSize, it.size)
            } else {
                beforeSize = it.size
                notifyDataSetChanged()
                DLog.e(TAG, "TESTSTESTETSES")
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_FIRST_GUIDE_CARD -> ImageGuideViewHolder(parent, ImageGuideUI())
            TYPE_ITEM_CARD -> ImageItemViewHolder(parent, ImageItemUI())
            TYPE_NOT_FOUND_CARD -> NotFoundGuideViewHolder(parent, NotFoundGuideUI())
            else -> EmptyItemViewHolder(parent, EmptyItemUI())
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_FIRST_GUIDE_CARD -> DLog.e(TAG, "TYPE_FIRST_GUIDE_CARD")
            TYPE_ITEM_CARD -> {
                holder as ImageItemViewHolder
                holder.onBind(viewModel.items.value ?: arrayListOf(), position)
            }
            TYPE_NOT_FOUND_CARD -> DLog.e(TAG, "TYPE_NOT_FOUND_CARD")
            else -> DLog.e(TAG, "TYPE_EMPTY_CARD")
        }
    }

    override fun getItemViewType(position: Int): Int {
        var size = realItemSize()
        viewModel.itemStatus.getDefault(ListItemStatus.FIRST).let {
            return when {
                size == 0 && it == ListItemStatus.FIRST -> TYPE_FIRST_GUIDE_CARD
                size == 0 && it == ListItemStatus.NOT_FOUND -> TYPE_NOT_FOUND_CARD
                size > 1 && it == ListItemStatus.FULL && position in 0 until itemCount -> TYPE_ITEM_CARD
                else -> TYPE_EMPTY_CARD
            }
        }
    }

    override fun getItemCount(): Int {
        var size = realItemSize()
        return when {
            size > 0 -> size
            else -> 1
        }
    }

    private fun realItemSize(): Int = viewModel.items.value?.size ?: 0

}