package com.sumin.vknewsclient.data.model.user


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("about")
    val about: String?,
    @SerializedName("bdate")
    val bdate: String?,
    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean?,
    @SerializedName("career")
    val career: List<Career?>?,
    @SerializedName("city")
    val city: City?,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("followers_count")
    val followersCount: Int?,
    @SerializedName("photo_200_orig")
    val photo: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("is_closed")
    val isClosed: Boolean?,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("counters")
    val counters: Counters,
    @SerializedName("online")
    val online: Int,
    @SerializedName("status")
    val status: String?
)