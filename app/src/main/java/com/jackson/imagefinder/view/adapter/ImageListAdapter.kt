package com.jackson.imagefinder.view.adapter

import android.view.ViewGroup
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

class ImageListAdapter(var viewModel: ImageViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TAG = ImageListAdapter::class.java.simpleName
    }

    // View 에서 Adapter의 갱신 종류를 판단하기 위해서 API 호출 전 items size 저장
    var beforeSize: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ListItemStatus.FIRST.id -> ImageGuideViewHolder(parent, ImageGuideUI())
            ListItemStatus.FULL.id -> ImageItemViewHolder(parent, ImageItemUI())
            ListItemStatus.NOT_FOUND.id -> NotFoundGuideViewHolder(parent, NotFoundGuideUI())
            else -> EmptyItemViewHolder(parent, EmptyItemUI()) // 예상밖의 예외를 대비하기 위해 비어있는 Holder를 반환하고 있음.
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ListItemStatus.FIRST.id -> DLog.i(TAG, "TYPE_FIRST_GUIDE_CARD")
            ListItemStatus.FULL.id -> with(holder as ImageItemViewHolder) {
                onBind(viewModel.items.value ?: arrayListOf(), position)
                DLog.i(TAG, "TYPE_ITEM_CARD")
            }
            ListItemStatus.NOT_FOUND.id -> DLog.i(TAG, "TYPE_NOT_FOUND_CARD")
            else -> DLog.i(TAG, "TYPE_EMPTY_CARD")
        }
    }

    override fun getItemViewType(position: Int): Int = with(viewModel) {
        var size = realItemSize()
        itemStatus.getDefault(ListItemStatus.FIRST).let {
            return when {
                size == 0 && it == ListItemStatus.FIRST -> ListItemStatus.FIRST.id
                size == 0 && it == ListItemStatus.NOT_FOUND -> ListItemStatus.NOT_FOUND.id
                size > 1 && it == ListItemStatus.FULL && position in 0 until itemCount -> ListItemStatus.FULL.id
                else -> ListItemStatus.EMPTY.id
            }
        }
    }

    override fun getItemCount(): Int = realItemSize().let { size -> if (size > 0) size else 1 }

    private fun realItemSize(): Int = viewModel.items.value?.size ?: 0

}