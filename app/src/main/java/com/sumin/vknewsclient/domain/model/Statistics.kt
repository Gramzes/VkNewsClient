package com.sumin.vknewsclient.domain.model

data class Statistics(
    val likes: Like,
    val shares: Share,
    val comments: Comments,
    val views: Views
)