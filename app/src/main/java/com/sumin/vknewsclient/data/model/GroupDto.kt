package com.sumin.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo_100")
    val photo: String
)
