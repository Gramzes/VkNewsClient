package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.User
import com.sumin.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProfileInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
    ) {

    operator fun invoke(): StateFlow<User?>{
        return repository.getProfileInfo()
    }
}