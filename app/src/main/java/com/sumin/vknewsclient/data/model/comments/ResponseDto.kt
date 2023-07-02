package com.sumin.vknewsclient.data.model.comments


import com.google.gson.annotations.SerializedName
import com.sumin.vknewsclient.data.model.GroupDto
import com.sumin.vknewsclient.data.model.ProfileDto

data class ResponseDto(
    @SerializedName("can_post")
    val canPost: Boolean,
    @SerializedName("count")
    val count: Int,
    @SerializedName("current_level_count")
    val currentLevelCount: Int,
    @SerializedName("groups")
    val groups: List<GroupDto>,
    @SerializedName("items")
    val comments: List<CommentDto>,
    @SerializedName("profiles")
    val profiles: List<ProfileDto>,
    @SerializedName("show_reply_button")
    val showReplyButton: Boolean
)