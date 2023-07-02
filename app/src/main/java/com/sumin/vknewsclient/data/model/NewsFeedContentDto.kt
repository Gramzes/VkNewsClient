package com.sumin.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class NewsFeedContentDto(
    @SerializedName("items")
    val posts: List<PostDto>,
    @SerializedName("profiles")
    val profiles: List<ProfileDto>,
    @SerializedName("groups")
    val groups: List<GroupDto>,
    @SerializedName("next_from")
    val nextStartKey: String
)
