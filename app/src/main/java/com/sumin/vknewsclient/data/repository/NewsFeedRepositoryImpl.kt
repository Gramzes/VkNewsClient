package com.sumin.vknewsclient.data.repository

import android.util.Log
import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.domain.repository.WallRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
): NewsFeedRepository {

    private val token: VKAccessToken?
        get() = VKAccessToken.restore(storage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val checkAuthResultEvents = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow{
        checkAuthResultEvents.emit(Unit)
        checkAuthResultEvents.collect {
            val currentToken = token
            if (currentToken != null && currentToken.isValid)
                emit(AuthState.Authorized)
            else
                emit(AuthState.NotAuthorized)
            token?.accessToken?.let { Log.d("TEST", it) }
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, AuthState.Initial)


    override fun getAuthState() = authStateFlow
    override suspend fun checkAuthResult() {
        checkAuthResultEvents.emit(Unit)
    }
}