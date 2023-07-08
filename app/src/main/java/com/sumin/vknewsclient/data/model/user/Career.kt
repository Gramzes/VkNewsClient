package com.sumin.vknewsclient.data.model.user


import com.google.gson.annotations.SerializedName

data class Career(
    @SerializedName("city_id")
    val cityId: Int?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("country_id")
    val countryId: Int?,
    @SerializedName("from")
    val from: Int?,
    @SerializedName("group_id")
    val groupId: Int?,
    @SerializedName("position")
    val position: String?,
    @SerializedName("until")
    val until: Int?
)