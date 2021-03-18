package com.jackson.imagefinder.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageSearchResponse(

    @SerializedName("documents")
    var documents: ArrayList<ImageData> = arrayListOf(),

    @SerializedName("meta")
    var meta: MetaData? = null

): Parcelable