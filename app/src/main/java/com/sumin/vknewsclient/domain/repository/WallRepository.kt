package com.sumin.vknewsclient.domain.repository

import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Resource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface WallRepository {

    fun getCommentsState(feedPost: FeedPost): StateFlow<List<Comment>>

    fun getWallState(): SharedFlow<Resource<List<FeedPost>>>

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun ignoreItem(feedPost: FeedPost)

    suspend fun loadNextData()
}