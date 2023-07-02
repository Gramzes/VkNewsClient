package com.sumin.vknewsclient.data.model.comments


import com.google.gson.annotations.SerializedName

data class ThreadCommentDto(
    @SerializedName("date")
    val date: Long,
    @SerializedName("from_id")
    val fromId: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("owner_id")
    val ownerId: Long,
    @SerializedName("parents_stack")
    val parentsStack: List<Int>,
    @SerializedName("post_id")
    val postId: Long,
    @SerializedName("reply_to_comment")
    val replyToComment: Long,
    @SerializedName("reply_to_user")
    val replyToUser: Long,
    @SerializedName("text")
    val text: String
)