package com.sumin.vknewsclient.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface GetPostsCountRepository {

    val wallPostsCount: StateFlow<Int?>
}