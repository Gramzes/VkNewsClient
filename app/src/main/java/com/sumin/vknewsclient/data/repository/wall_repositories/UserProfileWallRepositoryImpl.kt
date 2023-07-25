package com.sumin.vknewsclient.data.repository.wall_repositories

import android.util.Log
import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.model.NewsFeedResponseDto
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.di.qualifiers.UserProfile
import com.sumin.vknewsclient.di.scopes.ApplicationScope
import com.sumin.vknewsclient.domain.repository.GetPostsCountRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
@UserProfile
class UserProfileWallRepositoryImpl @Inject constructor(
    storage: VKPreferencesKeyValueStorage,
    mapper: NewsFeedMapper,
    private val vkService: ApiService,
): WallRepositoryImpl(storage, mapper, vkService), GetPostsCountRepository {

    private var _wallPostsCount = MutableStateFlow<Int?>(null)
    override val wallPostsCount = _wallPostsCount as StateFlow<Int?>

    private var offset = 0
    private val postsLoadCount = 20

    override suspend fun getNextData(token: String): NewsFeedResponseDto {
        val response = vkService.getWallPosts(
            accessToken = token,
            ownerId = 0L,
            count = postsLoadCount,
            offset = offset
        )
        _wallPostsCount.value = response.response.count
        offset += postsLoadCount
        return response
    }
}