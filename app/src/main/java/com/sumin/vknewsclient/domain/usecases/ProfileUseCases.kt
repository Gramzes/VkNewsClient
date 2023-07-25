package com.sumin.vknewsclient.domain.usecases

import com.sumin.vknewsclient.di.qualifiers.UserProfile
import com.sumin.vknewsclient.domain.repository.WallRepository
import javax.inject.Inject

class ProfileUseCases @Inject constructor(
    @UserProfile
    repository: WallRepository,
    changeLikeStatusFactory: ChangeLikeStatusUseCase.Factory,
    getPostsFactory: GetWallPostsUseCase.Factory,
    loadNextDataFactory: LoadNextDataUseCase.Factory,
    ignoreItemFactory: IgnoreItemUseCase.Factory,
    val getPostCountUseCase: GetPostsCountUseCase,
    val getProfileInfoUseCase: GetProfileInfoUseCase,
    val getProfilePhotosUseCase: GetProfilePhotosUseCase,
    val getFriendsForProfileUseCase: GetFriendsForProfileUseCase
) {
    val getPostsUseCase = getPostsFactory.create(repository)
    val loadNextDataUseCase = loadNextDataFactory.create(repository)
    val changeLikeStatusUseCase = changeLikeStatusFactory.create(repository)
    val ignoreItemUseCase = ignoreItemFactory.create(repository)
}