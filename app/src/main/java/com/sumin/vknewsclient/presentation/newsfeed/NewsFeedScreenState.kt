package com.sumin.vknewsclient.presentation.newsfeed

import com.sumin.vknewsclient.domain.model.FeedPost

sealed class NewsFeedScreenState{

    object Initial: NewsFeedScreenState()

    object Loading: NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val isNextLoading: Boolean = false
    ): NewsFeedScreenState()
}