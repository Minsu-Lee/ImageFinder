package com.jackson.imagefinder.view.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackson.imagefinder.R
import com.jackson.imagefinder.extensions.dateFormat
import com.jackson.imagefinder.extensions.toDate
import com.jackson.imagefinder.model.ImageData
import com.jackson.imagefinder.utils.StringUtils
import com.jackson.imagefinder.view.adapter.ui.ImageItemUI
import org.jetbrains.anko.AnkoContext

class ImageItemViewHolder(parent: ViewGroup, val ui: ImageItemUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent))) {

     fun onBind(items: ArrayList<ImageData>, position: Int) = with(ui) {
          items[position].let { item ->

               Glide.with(imageIv.context)
                    .load(item.thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(imageIv)

               StringUtils.defaultStr(item.collection).let { collection ->
                    imageCategoryTv.visibility = if (collection.isEmpty()) View.GONE else View.VISIBLE
                    imageCategoryTv.text = collection
               }

               StringUtils.defaultStr(item.displaySitename).let { sitename ->
                    imageSiteTv.visibility = if (sitename.isEmpty()) View.GONE else View.VISIBLE
                    imageSiteTv.text = sitename
               }

               StringUtils.defaultStr(item.datetime).let { datetime ->
                    imageDateTv.visibility = if (datetime.isEmpty()) View.GONE else View.VISIBLE
                    imageDateTv.text = datetime.toDate("YYYY-MM-DD'T'hh:mm:ss.000")
                         .dateFormat("yyyy-MM-dd HH:mm")
               }

               imageCard.setOnClickListener {
                    // 이미지 상세화면 호출
                    Toast.makeText(imageCard.context, item.datetime, Toast.LENGTH_LONG).show()
               }

          }
     }
}