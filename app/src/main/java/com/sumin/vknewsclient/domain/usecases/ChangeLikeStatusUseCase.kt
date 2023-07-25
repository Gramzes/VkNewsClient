package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.domain.repository.WallRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class ChangeLikeStatusUseCase @AssistedInject constructor(
    @Assisted private val repository: WallRepository
    ) {

    suspend operator fun invoke(feedPost: FeedPost){
        return repository.changeLikeStatus(feedPost)
    }

    @AssistedFactory
    interface Factory{
        fun create(repository: WallRepository): ChangeLikeStatusUseCase
    }
}