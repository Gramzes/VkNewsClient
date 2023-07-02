package com.sumin.vknewsclient.domain.model

sealed class AuthState{
    object Authorized: AuthState()
    object NotAuthorized: AuthState()
    object Initial: AuthState()
}
