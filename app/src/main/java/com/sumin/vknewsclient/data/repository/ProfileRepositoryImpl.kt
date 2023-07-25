package com.sumin.vknewsclient.data.repository

import android.util.Log
import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.mapper.UserMapper
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Like
import com.sumin.vknewsclient.domain.model.User
import com.sumin.vknewsclient.domain.repository.ProfileRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val vkService: ApiService,
    private val userMapper: UserMapper,
): ProfileRepository {

    private val token: VKAccessToken?
        get() = VKAccessToken.restore(storage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler{ _, ex ->
        Log.d("Test", ex.toString())
    })

    private fun getAccessToken(): String{
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    private fun getUserId(): Long = token?.userId?.value ?: throw IllegalStateException("Token is null")

    override fun getProfileInfo(): StateFlow<User?> = flow {
        val response = vkService.getUser(getAccessToken(), getUserId())
        val user = userMapper.mapGetUserResponseToUser(response)
        emit(user)
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    override fun getProfilePhotos() = flow {
        val response = vkService.getProfilePhotos(getAccessToken(), getUserId())
        val photos = userMapper.mapGetPhotoResponseToPhotos(response)
        emit(photos)
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    override fun getFriends() = flow {
        val response = vkService.getFriends(getAccessToken(), getUserId(), 3)
        val users = userMapper.mapGetFriendsResponseToUsers(response)
        emit(users)
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)


    companion object{
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}