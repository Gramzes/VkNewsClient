package com.sumin.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class PhotoUrlDto(
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val size: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)
