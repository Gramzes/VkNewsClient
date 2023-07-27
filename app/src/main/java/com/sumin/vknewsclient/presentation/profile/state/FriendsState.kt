package com.sumin.vknewsclient.presentation.profile.state

import com.sumin.vknewsclient.domain.model.User

sealed interface FriendsState {

    object Initial: FriendsState

    data class Friends(val count: Int, val friends: List<User>): FriendsState

    object NoFriends: FriendsState
}