package com.sumin.vknewsclient.data.repository

import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Like
import com.sumin.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

class WallRepositoryImpl (
    private val mapper: NewsFeedMapper,
    private val vkService: ApiService,
    private val token: String
): WallRepository {

    private var isFirstLoading = true
    private var nextFromKey: String? = null

    private var _wallPosts = mutableListOf<FeedPost>()
    private val wallPosts: List<FeedPost>
        get() = _wallPosts.toList()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataIsNeeded = MutableSharedFlow<Unit>(replay = 1)

    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val wallState: StateFlow<List<FeedPost>> = flow {
        nextDataIsNeeded.emit(Unit)
        nextDataIsNeeded.collect {
            if (!isFirstLoading && nextFromKey == null) {
                emit(wallPosts)
                return@collect
            }
            val response = vkService.loadNewsFeed(token, nextFromKey)
            nextFromKey = response.response.nextStartKey
            isFirstLoading = false
            _wallPosts += mapper.mapResponseToPosts(response).toMutableList()
            emit(wallPosts)
        }
    }
        .retry{
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
        .mergeWith(refreshedListFlow)
        .stateIn(coroutineScope, SharingStarted.Lazily, wallPosts)

    override suspend fun loadNextData(){
        nextDataIsNeeded.emit(Unit)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost){
        val response = if (feedPost.statistics.likes.isLiked){
            vkService.deleteLike(token, feedPost.ownerId, feedPost.id)
        } else {
            vkService.addLike(token, feedPost.ownerId, feedPost.id)
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
        vkService.ignoreItem(token, feedPost.ownerId, feedPost.id)
        _wallPosts.remove(feedPost)
        refreshedListFlow.emit(wallPosts)
    }

    override fun getCommentsState(feedPost: FeedPost) = flow {
        val response = vkService.getComments(token, feedPost.ownerId, feedPost.id)
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