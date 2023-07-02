package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetAuthStateUseCase(private val repository: NewsFeedRepository) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthState()
    }
}