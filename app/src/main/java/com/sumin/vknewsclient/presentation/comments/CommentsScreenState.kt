package com.sumin.vknewsclient.presentation.comments

import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost

sealed class CommentsScreenState{

    object Initial: CommentsScreenState()

    object Loading: CommentsScreenState()

    data class Comments(val feedPost: FeedPost, val comments: List<Comment>): CommentsScreenState()
}
