package com.sumin.vknewsclient.data.model.comments


import com.google.gson.annotations.SerializedName

data class ThreadDto(
    @SerializedName("can_post")
    val canPost: Boolean,
    @SerializedName("count")
    val count: Int,
    @SerializedName("items")
    val items: List<ThreadCommentDto>,
    @SerializedName("show_reply_button")
    val showReplyButton: Boolean
)