package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class GetFriendsForProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    operator fun invoke() = repository.getFriends()
}