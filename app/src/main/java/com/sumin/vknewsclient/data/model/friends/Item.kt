package com.sumin.vknewsclient.data.model.friends


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_50")
    val photo50: String,
    @SerializedName("track_code")
    val trackCode: String
)