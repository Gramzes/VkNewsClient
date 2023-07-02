package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository

class IgnoreItemUseCase(private val repository: NewsFeedRepository) {

    suspend operator fun invoke(feedPost: FeedPost){
        return repository.changeLikeStatus(feedPost)
    }
}