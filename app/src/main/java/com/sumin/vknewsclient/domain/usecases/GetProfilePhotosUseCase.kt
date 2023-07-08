package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProfilePhotosUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    operator fun invoke(): StateFlow<List<Photo>> {
        return repository.getProfilePhotos()
    }
}