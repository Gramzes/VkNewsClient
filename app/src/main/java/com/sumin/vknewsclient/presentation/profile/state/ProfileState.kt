package com.sumin.vknewsclient.presentation.profile.state

import com.sumin.vknewsclient.domain.model.User

sealed interface ProfileState {

    object Initial: ProfileState

    data class Profile(val profile: User): ProfileState
}