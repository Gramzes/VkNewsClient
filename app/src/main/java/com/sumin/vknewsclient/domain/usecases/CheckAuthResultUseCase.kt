package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class CheckAuthResultUseCase @Inject constructor(private val repository: NewsFeedRepository) {

    suspend operator fun invoke(){
        return repository.checkAuthResult()
    }
}