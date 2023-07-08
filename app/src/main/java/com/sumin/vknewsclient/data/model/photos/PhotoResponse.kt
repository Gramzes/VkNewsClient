package com.sumin.vknewsclient.data.model.photos

import com.google.gson.annotations.SerializedName
import com.sumin.vknewsclient.data.model.PhotoDto

data class PhotoResponse(
    @SerializedName("items")
    val items: List<PhotoDto>
)
