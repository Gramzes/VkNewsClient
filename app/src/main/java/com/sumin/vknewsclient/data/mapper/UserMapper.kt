package com.sumin.vknewsclient.data.mapper

import com.sumin.vknewsclient.data.model.friends.GetFriendsResponse
import com.sumin.vknewsclient.data.model.photos.GetPhotoResponse
import com.sumin.vknewsclient.data.model.user.GetUserResponse
import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun mapGetUserResponseToUser(response: GetUserResponse): User{
        val userInfo = response.response[0]
        return User(
            id = userInfo.id,
            firstName = userInfo.firstName,
            lastName = userInfo.lastName,
            photoUrl =userInfo.photo,
            online = userInfo.online == 1,
            status = userInfo.status,
            city = userInfo.city?.title,
            bDate = userInfo.bdate,
            work = userInfo.career?.lastOrNull()?.company,
            followersCount = userInfo.followersCount,
            friendsCount = userInfo.counters.friends
        )
    }

    fun mapGetPhotoResponseToPhotos(response: GetPhotoResponse): List<Photo>{
        return response.response.items.map {
            val smallPhoto = it.photoUrl.find {
                it.size == "s"
            }!!.url
            val mediumPhoto = it.photoUrl.find {
                it.size == "x"
            }!!.url
            var maxPhoto = it.photoUrl.find {
                it.size == "w"
            }?.url
            if (maxPhoto == null){
                maxPhoto = it.photoUrl.find {
                    it.size == "z"
                }?.url
            }
            if (maxPhoto == null){
                maxPhoto = it.photoUrl.find {
                    it.size == "y"
                }?.url
            }
            if (maxPhoto == null){
                maxPhoto = mediumPhoto
            }
            Photo(
                id = it.id.toLong(),
                smallSizeUrl = smallPhoto,
                mediumSizeUrl = mediumPhoto,
                maxSizeUrl = maxPhoto
            )
        }
    }

    fun mapGetFriendsResponseToUsers(response: GetFriendsResponse): List<User>{
        return response.response.items.map {
            User(it.id, it.firstName, it.lastName, it.photo50)
        }
    }
}