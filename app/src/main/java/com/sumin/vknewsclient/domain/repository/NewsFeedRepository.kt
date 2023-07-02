package com.sumin.vknewsclient.domain.repository


import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost
import kotlinx.coroutines.flow.StateFlow


interface NewsFeedRepository {

    fun getCommentsState(feedPost: FeedPost): StateFlow<List<Comment>>

    fun getNewsFeedState(): StateFlow<List<FeedPost>>

    fun getAuthState(): StateFlow<AuthState>

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun ignoreItem(feedPost: FeedPost)

    suspend fun checkAuthResult()

    suspend fun loadNextData()
}