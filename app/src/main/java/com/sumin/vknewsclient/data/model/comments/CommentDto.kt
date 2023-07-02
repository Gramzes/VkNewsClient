package com.sumin.vknewsclient.data.model.comments


import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("date")
    val date: Long,
    @SerializedName("from_id")
    val fromId: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("owner_id")
    val ownerId: Long,
    @SerializedName("parents_stack")
    val parentsStack: List<Any>,
    @SerializedName("post_id")
    val postId: Long,
    @SerializedName("text")
    val text: String,
    @SerializedName("thread")
    val thread: ThreadDto
)