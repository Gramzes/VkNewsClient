package com.sumin.vknewsclient.data.repository.wall_repositories

import com.sumin.vknewsclient.data.mapper.NewsFeedMapper
import com.sumin.vknewsclient.data.model.NewsFeedResponseDto
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.di.qualifiers.NewsFeed
import com.sumin.vknewsclient.di.scopes.ApplicationScope
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import javax.inject.Inject

@ApplicationScope
@NewsFeed
class NewsFeedWallRepositoryImpl @Inject constructor(
    storage: VKPreferencesKeyValueStorage,
    mapper: NewsFeedMapper,
    private val vkService: ApiService,
): WallRepositoryImpl(storage, mapper, vkService) {

    private var nextFromKey: String? = null

    override suspend fun getNextData(token: String): NewsFeedResponseDto {
        val response = vkService.loadNewsFeed(token, nextFromKey)
        nextFromKey = response.response.nextStartKey
        return response
    }
}