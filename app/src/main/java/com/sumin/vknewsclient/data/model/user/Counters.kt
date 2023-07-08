package com.sumin.vknewsclient.data.model.user

import com.google.gson.annotations.SerializedName

data class Counters(
    @SerializedName("friends")
    val friends: Int
)
