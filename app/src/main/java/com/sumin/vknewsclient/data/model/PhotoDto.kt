package com.sumin.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("sizes")
    val photoUrl: List<PhotoUrlDto>
)
