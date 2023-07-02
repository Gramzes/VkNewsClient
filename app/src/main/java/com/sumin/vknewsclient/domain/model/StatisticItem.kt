package com.sumin.vknewsclient.domain.model

interface StatisticItem{
    val count: Int
}
data class Like(override val count: Int, val isLiked: Boolean): StatisticItem
data class Share(override val count: Int): StatisticItem
data class Comments(override val count: Int): StatisticItem
data class Views(override val count: Int): StatisticItem
