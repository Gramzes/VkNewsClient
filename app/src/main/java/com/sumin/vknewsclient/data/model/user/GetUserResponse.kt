package com.sumin.vknewsclient.data.model.user


import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("response")
    val response: List<Response>
)