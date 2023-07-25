package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.di.qualifiers.UserProfile
import com.sumin.vknewsclient.domain.repository.GetPostsCountRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetPostsCountUseCase @Inject constructor(
    @UserProfile
    private val repository: GetPostsCountRepository
) {

    operator fun invoke(): StateFlow<Int?> = repository.wallPostsCount

}