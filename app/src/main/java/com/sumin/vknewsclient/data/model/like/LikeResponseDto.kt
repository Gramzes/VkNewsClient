package com.sumin.vknewsclient.data.model.like

import com.google.gson.annotations.SerializedName

data class LikeResponseDto(
    @SerializedName("response")
    val response: LikeDto
)
