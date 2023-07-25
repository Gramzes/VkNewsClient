package com.sumin.vknewsclient.domain.model

data class Photo(
    val id: Long,
    val photoSizesRatio: Float,
    val smallSizeUrl: String,
    val mediumSizeUrl: String,
    val maxSizeUrl: String
)
