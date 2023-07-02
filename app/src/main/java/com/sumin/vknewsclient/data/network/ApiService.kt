package com.sumin.vknewsclient.data.network

import com.sumin.vknewsclient.data.model.NewsFeedResponseDto
import com.sumin.vknewsclient.data.model.comments.CommentsResponseDto
import com.sumin.vknewsclient.data.model.like.LikeResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.131&filters=post")
    suspend fun loadNewsFeed(
        @Query("access_token") accessToken: String,
        @Query("start_from") startFrom: String?
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
}