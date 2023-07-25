package com.sumin.vknewsclient.presentation.profile.state

sealed interface ProfileScreenState{

    object Initial: ProfileScreenState

    object Loading: ProfileScreenState

    data class ProfileInfo(
        val profileState: ProfileState,
        val friendsState: FriendsState,
        val photosState: PhotosState
    ): ProfileScreenState
}