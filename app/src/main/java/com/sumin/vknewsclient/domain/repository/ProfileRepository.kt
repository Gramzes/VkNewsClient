package com.sumin.vknewsclient.domain.repository

import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {

    fun getWallPostsState(): StateFlow<List<FeedPost>>

    suspend fun loadNextWallPosts()

    fun getProfileInfo(): StateFlow<User?>

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun ignoreItem(feedPost: FeedPost)
    fun getProfilePhotos(): StateFlow<List<Photo>>
    fun getFriends(): StateFlow<List<User>>
}