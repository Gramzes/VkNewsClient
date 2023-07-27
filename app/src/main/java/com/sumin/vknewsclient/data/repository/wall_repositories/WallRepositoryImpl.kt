package com.sumin.vknewsclient.data.repository.wall_repositories

import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.model.NewsFeedResponseDto
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Like
import com.sumin.vknewsclient.domain.model.Resource
import com.sumin.vknewsclient.domain.repository.WallRepository
import com.sumin.vknewsclient.extensions.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn


abstract class WallRepositoryImpl constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val mapper: NewsFeedMapper,
    private val vkService: ApiService,
): WallRepository {

    private val token: VKAccessToken?
        get() = VKAccessToken.restore(storage)

    private var _wallPosts = mutableListOf<FeedPost>()
    private val wallPosts: List<FeedPost>
        get() = _wallPosts.toList()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataIsNeeded = MutableSharedFlow<Unit>(replay = 1)

    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
    private val refreshedStateFlow = flow{
        refreshedListFlow.collect{
            val currentState = wallState.first()
            emit(currentState.copy(it))
        }
    }.shareIn(coroutineScope, SharingStarted.Lazily)

    abstract suspend fun getNextData(token: String): NewsFeedResponseDto

    private val wallState: SharedFlow<Resource<List<FeedPost>>> = flow {
        nextDataIsNeeded.emit(Unit)
        nextDataIsNeeded.collect {
            emit(Resource.Loading(wallPosts))
            val response = getNextData(getAccessToken())
            val newPosts = mapper.mapResponseToPosts(response).toMutableList()
            _wallPosts += newPosts
            delay(4000)
            if (newPosts.isEmpty()){
                emit(Resource.EndOfData(wallPosts))
            } else {
                emit(Resource.Data(wallPosts))
            }
        }
    }
        .retry{
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
        .mergeWith(refreshedStateFlow)
        .shareIn(coroutineScope, SharingStarted.Lazily, replay = 1)

    private fun getAccessToken(): String{
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    override suspend fun loadNextData(){
        nextDataIsNeeded.emit(Unit)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost){
        val response = if (feedPost.statistics.likes.isLiked){
            vkService.deleteLike(getAccessToken(), feedPost.ownerId, feedPost.id)
        } else {
            vkService.addLike(getAccessToken(), feedPost.ownerId, feedPost.id)
        }
        val likesCount = response.response.likes
        val newStatistics = feedPost.statistics.copy(
            likes = Like(likesCount, !feedPost.statistics.likes.isLiked)
        )
        val newPost = feedPost.copy(
            statistics = newStatistics
        )
        val index = _wallPosts.indexOf(feedPost)
        _wallPosts[index] = newPost
        refreshedListFlow.emit(wallPosts)
    }

    override suspend fun ignoreItem(feedPost: FeedPost){
        vkService.ignoreItem(getAccessToken(), feedPost.ownerId, feedPost.id)
        _wallPosts.remove(feedPost)
        refreshedListFlow.emit(wallPosts)
    }

    override fun getCommentsState(feedPost: FeedPost) = flow {
        val response = vkService.getComments(getAccessToken(), feedPost.ownerId, feedPost.id)
        emit(mapper.mapResponseToComments(response))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(coroutineScope, SharingStarted.Lazily, listOf())

    override fun getWallState() = wallState

    companion object{
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}