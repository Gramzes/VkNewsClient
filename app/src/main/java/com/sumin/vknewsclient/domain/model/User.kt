package com.sumin.vknewsclient.domain.model

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val photoUrl: String,
    val online: Boolean? = null,
    val status: String? = null,
    val city: String? = null,
    val bDate: String? = null,
    val work: String? = null,
    val followersCount: Int? = null,
    val friendsCount: Int? = null
)
