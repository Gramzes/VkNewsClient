package com.sumin.vknewsclient.domain.model

data class FeedPost(
    val id: Long,
    val ownerId: Long,
    val communityName: String = "/dev/null",
    val publicationDate: String = "14:00",
    val avatarUrl: String,
    val contentText: String = "Lorem Ipsum - это текст-\"рыба\", часто используемый в печати и вэб-дизайне. Lorem Ipsum является стандартной \"рыбой\" для текстов на латинице с начала XVI века.",
    val contentImages: List<Photo>?,
    val statistics: Statistics
)

