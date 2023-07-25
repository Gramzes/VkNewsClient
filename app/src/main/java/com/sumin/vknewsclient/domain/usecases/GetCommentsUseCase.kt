package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.domain.repository.WallRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCommentsUseCase @AssistedInject constructor(
    @Assisted
    private val repository: WallRepository
    ) {

    operator fun invoke(feedPost: FeedPost): StateFlow<List<Comment>> {
        return repository.getCommentsState(feedPost)
    }

    @AssistedFactory
    interface Factory{
        fun create(repository: WallRepository): GetCommentsUseCase
    }
}