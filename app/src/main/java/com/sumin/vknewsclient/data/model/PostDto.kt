package com.sumin.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("source_id")
    val sourceId: Long,
    @SerializedName("date")
    val date: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("likes")
    val likes: LikesDto,
    @SerializedName("comments")
    val comments: CommentsDto,
    @SerializedName("reposts")
    val reposts: RepostsDto,
    @SerializedName("views")
    val views: ViewsDto?,
    @SerializedName("attachments")
    val attachments: List<AttachmentDto>?
)
