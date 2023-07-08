package com.sumin.vknewsclient.data.repository

import android.util.Log
import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Like
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.extensions.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val mapper: NewsFeedMapper,
    private val vkService: ApiService
): NewsFeedRepository {

    private val token: VKAccessToken?
        get() = VKAccessToken.restore(storage)
    private var isFirstLoading = true
    private var nextFromKey: String? = null
    private var _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataIsNeeded = MutableSharedFlow<Unit>(replay = 1)

    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val newsFeedState: StateFlow<List<FeedPost>> = flow {
        nextDataIsNeeded.emit(Unit)
        nextDataIsNeeded.collect {
            if (!isFirstLoading && nextFromKey == null) {
                emit(feedPosts)
                return@collect
            }
            val accessToken = getAccessToken()
            val response = vkService.loadNewsFeed(accessToken, nextFromKey)
            nextFromKey = response.response.nextStartKey
            isFirstLoading = false
            _feedPosts += mapper.mapResponseToPosts(response).toMutableList()
            emit(feedPosts)
        }
    }
        .retry{
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
        .mergeWith(refreshedListFlow)
        .stateIn(coroutineScope, SharingStarted.Lazily, feedPosts)

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

    override suspend fun checkAuthResult(){
        checkAuthResultEvents.emit(Unit)
    }

    override suspend fun loadNextData(){
        nextDataIsNeeded.emit(Unit)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost){
        val accessToken = getAccessToken()
        val response = if (feedPost.statistics.likes.isLiked){
            vkService.deleteLike(accessToken, feedPost.ownerId, feedPost.id)
        } else {
            vkService.addLike(accessToken, feedPost.ownerId, feedPost.id)
        }
        val likesCount = response.response.likes
        val newStatistics = feedPost.statistics.copy(
            likes = Like(likesCount, !feedPost.statistics.likes.isLiked)
        )
        val newPost = feedPost.copy(
            statistics = newStatistics
        )
        val index = _feedPosts.indexOf(feedPost)
        _feedPosts[index] = newPost
        refreshedListFlow.emit(feedPosts)
    }

    override suspend fun ignoreItem(feedPost: FeedPost){
        val accessToken = getAccessToken()
        vkService.ignoreItem(accessToken, feedPost.ownerId, feedPost.id)
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    override fun getCommentsState(feedPost: FeedPost) = flow {
        val accessToken = getAccessToken()
        val response = vkService.getComments(accessToken, feedPost.ownerId, feedPost.id)
        emit(mapper.mapResponseToComments(response))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(coroutineScope, SharingStarted.Lazily, listOf())

    override fun getNewsFeedState() = newsFeedState

    override fun getAuthState() = authStateFlow

    private fun getAccessToken(): String{
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    companion object{
        const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}