package com.sumin.vknewsclient.data.model.comments


import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response")
    val response: ResponseDto
)