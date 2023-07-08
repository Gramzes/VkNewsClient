package com.sumin.vknewsclient.presentation.profile

import androidx.lifecycle.ViewModel
import com.sumin.vknewsclient.domain.usecases.GetFriendsForProfileUseCase
import com.sumin.vknewsclient.domain.usecases.GetProfileInfoUseCase
import com.sumin.vknewsclient.domain.usecases.GetProfilePhotosUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getProfilePhotosUseCase: GetProfilePhotosUseCase,
    private val getFriendsForProfileUseCase: GetFriendsForProfileUseCase
): ViewModel() {
    val profileState = getProfileInfoUseCase()
        .filterNotNull()

    val profilePhotosState = getProfilePhotosUseCase()

    val friendsState = getFriendsForProfileUseCase()
        .filter { it.isNotEmpty() }
}