package com.sumin.vknewsclient.data.network

import androidx.compose.ui.geometry.Offset
import com.sumin.vknewsclient.data.model.NewsFeedResponseDto
import com.sumin.vknewsclient.data.model.comments.CommentsResponseDto
import com.sumin.vknewsclient.data.model.friends.GetFriendsResponse
import com.sumin.vknewsclient.data.model.like.LikeResponseDto
import com.sumin.vknewsclient.data.model.photos.GetPhotoResponse
import com.sumin.vknewsclient.data.model.user.GetUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.131&filters=post")
    suspend fun loadNewsFeed(
        @Query("access_token") accessToken: String,
        @Query("start_from") startFrom: String?
    ): NewsFeedResponseDto

    @GET("wall.get?v=5.131&extended=1")
    suspend fun getWallPosts(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("count") count: Int,
        @Query("offset") offset: Int,
    ): NewsFeedResponseDto

    @GET("likes.add?v=5.131&type=post")
    suspend fun addLike(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): LikeResponseDto

    @GET("likes.delete?v=5.131&type=post")
    suspend fun deleteLike(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): LikeResponseDto

    @GET("newsfeed.ignoreItem?v=5.131&type=wall")
    suspend fun ignoreItem(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    )

    @GET("wall.getComments?v=5.131&extended=1&fields=photo_100&thread_items_count=3")
    suspend fun getComments(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long
    ): CommentsResponseDto

    @GET("users.get?v=5.131&fields=bdate,status,career,city,followers_count,online,photo_200_orig,counters")
    suspend fun getUser(
        @Query("access_token") accessToken: String,
        @Query("user_ids") userId: Long,
    ): GetUserResponse

    @GET("photos.get?v=5.131&album_id=profile&rev=1&count=6")
    suspend fun getProfilePhotos(
        @Query("access_token") accessToken: String,
        @Query("owner_id") userId: Long,
    ): GetPhotoResponse

    @GET("friends.get?v=5.131&order=random&fields=photo_50")
    suspend fun getFriends(
        @Query("access_token") accessToken: String,
        @Query("user_id") userId: Long,
        @Query("count") count: Int
    ): GetFriendsResponse
}