package com.sumin.vknewsclient.domain.repository


import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost
import kotlinx.coroutines.flow.StateFlow


interface NewsFeedRepository {

    fun getAuthState(): StateFlow<AuthState>

    suspend fun checkAuthResult()
}