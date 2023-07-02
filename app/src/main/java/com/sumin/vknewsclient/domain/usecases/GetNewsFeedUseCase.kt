package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetNewsFeedUseCase(private val repository: NewsFeedRepository) {

    operator fun invoke(): StateFlow<List<FeedPost>> {
        return repository.getNewsFeedState()
    }
}