package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetCommentsUseCase(private val repository: NewsFeedRepository) {

    operator fun invoke(feedPost: FeedPost): StateFlow<List<Comment>> {
        return repository.getCommentsState(feedPost)
    }
}