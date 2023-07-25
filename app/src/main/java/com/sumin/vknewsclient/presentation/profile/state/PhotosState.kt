package com.sumin.vknewsclient.presentation.profile.state

import com.sumin.vknewsclient.domain.model.Photo

sealed interface PhotosState{

    object Initial: PhotosState

    data class Photos(val photos: List<Photo>): PhotosState

    object NoPhotos: PhotosState
}