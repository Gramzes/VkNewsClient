package com.sumin.vknewsclient.data.model.photos

import com.google.gson.annotations.SerializedName

data class GetPhotoResponse(
    @SerializedName("response")
    val response: PhotoResponse
)
