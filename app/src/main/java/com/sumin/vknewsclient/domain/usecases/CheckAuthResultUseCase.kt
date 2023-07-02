package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.repository.NewsFeedRepository

class CheckAuthResultUseCase(private val repository: NewsFeedRepository) {

    suspend operator fun invoke(){
        return repository.checkAuthResult()
    }
}