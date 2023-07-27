package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Resource
import com.sumin.vknewsclient.domain.repository.WallRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharedFlow

class GetWallPostsUseCase @AssistedInject constructor(
    @Assisted
    private val repository: WallRepository
    ) {

    operator fun invoke(): SharedFlow<Resource<List<FeedPost>>> {
        return repository.getWallState()
    }

    @AssistedFactory
    interface Factory{
        fun create(repository: WallRepository): GetWallPostsUseCase
    }
}