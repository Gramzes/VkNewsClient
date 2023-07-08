package com.sumin.vknewsclient.data.model.friends


import com.google.gson.annotations.SerializedName

data class GetFriendsResponse(
    @SerializedName("response")
    val response: Response
)