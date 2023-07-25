package com.sumin.vknewsclient.data.mapper

import com.sumin.vknewsclient.data.model.NewsFeedResponseDto
import com.sumin.vknewsclient.data.model.PhotoDto
import com.sumin.vknewsclient.data.model.comments.CommentsResponseDto
import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.Comments
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Like
import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.model.Share
import com.sumin.vknewsclient.domain.model.Statistics
import com.sumin.vknewsclient.domain.model.Views
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NewsFeedMapper @Inject constructor() {

    fun mapResponseToPosts(response: NewsFeedResponseDto): List<FeedPost>{
        val posts = response.response.posts
        val groups = response.response.groups
        val profiles = response.response.profiles

        val feedPosts = posts.map{
            var communityName: String? = null
            var avatarUrl: String? = null

            val sourceId = it.sourceId ?: it.ownerId ?: throw IllegalStateException()
            if (sourceId < 0) {
                val group = groups.find { group ->
                    group.id == -sourceId
                }
                communityName = group?.name
                avatarUrl = group?.photo
            } else{
                val profile = profiles.find { profile ->
                    profile.id == sourceId
                }
                profile?.let {
                    communityName = "${profile.firstName} ${profile.lastName}"
                    avatarUrl = profile.photo
                }
            }
            it.attachments?.map { it.photo }

            val photos = it.attachments?.mapNotNull {
                it.photo?.let { it1 -> getPhoto(it1) }
            }

            FeedPost(
                id = it.id,
                ownerId = sourceId,
                communityName = communityName ?: "",
                publicationDate = mapTimestampToDate(it.date * 1000L),
                avatarUrl = avatarUrl ?: "",
                contentText = it.text,
                contentImages = photos,
                statistics = Statistics(
                    Like(it.likes.count, it.likes.isLiked == 1),
                    Share(it.reposts.count),
                    Comments(it.comments.count),
                    Views(it.views?.count ?: 0)
                )
            )
        }
        return feedPosts
    }

    private fun getPhoto(photo: PhotoDto): Photo {
        val smallPhoto = photo.photoUrl.find {
            it.size == "s"
        }!!
        val mediumPhoto = photo.photoUrl.find {
            it.size == "x"
        }!!
        var maxPhoto = photo.photoUrl.find {
            it.size == "w"
        }
        if (maxPhoto == null){
            maxPhoto = photo.photoUrl.find {
                it.size == "z"
            }
        }
        if (maxPhoto == null){
            maxPhoto = photo.photoUrl.find {
                it.size == "y"
            }
        }
        if (maxPhoto == null){
            maxPhoto = mediumPhoto
        }
        return Photo(
            id = photo.id.toLong(),
            photoSizesRatio = smallPhoto.width / smallPhoto.height.toFloat(),
            smallSizeUrl = smallPhoto.url,
            mediumSizeUrl = mediumPhoto.url,
            maxSizeUrl = maxPhoto.url
        )
    }

    fun mapResponseToComments(response: CommentsResponseDto): List<Comment>{
        return response.response.comments.map {comment ->
            var authorName = ""
            var authorPhotoUrl = ""

            if (comment.fromId < 0){
                response.response.groups.find {
                    it.id == -comment.fromId
                }?.let {
                    authorName = it.name
                    authorPhotoUrl = it.photo
                }

            } else {
                response.response.profiles.find {
                    it.id == comment.fromId
                }?.let {
                    authorName = "${it.firstName} ${it.lastName}"
                    authorPhotoUrl = it.photo
                }
            }
            Comment(
                id = comment.id,
                authorName = authorName,
                authorAvatarUrl = authorPhotoUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date * 1000)
            )
        }
    }

    fun mapTimestampToDate(timestamp: Long): String{
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault()).format(date)
    }
}