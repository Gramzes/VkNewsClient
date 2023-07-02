package com.sumin.vknewsclient.domain.model

data class Comment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationDate: String
)
