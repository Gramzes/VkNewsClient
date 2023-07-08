package com.sumin.vknewsclient.data.model.friends


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("count")
    val count: Int,
    @SerializedName("items")
    val items: List<Item>
)