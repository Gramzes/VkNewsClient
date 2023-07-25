package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.repository.WallRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class LoadNextDataUseCase @AssistedInject constructor(
    @Assisted
    private val repository: WallRepository
    ) {

    suspend operator fun invoke(){
        return repository.loadNextData()
    }

    @AssistedFactory
    interface Factory{
        fun create(repository: WallRepository): LoadNextDataUseCase
    }
}