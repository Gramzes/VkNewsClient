package com.sumin.vknewsclient.presentation.profile.state

import com.sumin.vknewsclient.domain.model.FeedPost

sealed interface PostsState {

    object Initial: PostsState

    object Loading: PostsState

    data class Posts(
        val allPostsCount: Int,
        val posts: List<FeedPost>,
        val isNextLoading: Boolean = false,
        val endOfData: Boolean = false
    ): PostsState

}