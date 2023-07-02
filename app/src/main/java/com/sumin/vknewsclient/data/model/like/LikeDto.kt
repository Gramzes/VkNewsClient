package com.sumin.vknewsclient.data.model.like

import com.google.gson.annotations.SerializedName

data class LikeDto(
    @SerializedName("likes")
    val likes: Int
)