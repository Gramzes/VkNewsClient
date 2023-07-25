package com.sumin.vknewsclient.domain.repository

import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {

    fun getProfileInfo(): StateFlow<User?>
    fun getProfilePhotos(): StateFlow<List<Photo>?>
    fun getFriends(): StateFlow<List<User>?>
}