package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: NewsFeedRepository
    ) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthState()
    }
}