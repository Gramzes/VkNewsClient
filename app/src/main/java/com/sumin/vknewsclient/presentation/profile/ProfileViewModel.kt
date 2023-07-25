package com.sumin.vknewsclient.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumin.vknewsclient.di.qualifiers.NewsFeed
import com.sumin.vknewsclient.di.qualifiers.UserProfile
import com.sumin.vknewsclient.domain.repository.WallRepository
import com.sumin.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.sumin.vknewsclient.domain.usecases.GetFriendsForProfileUseCase
import com.sumin.vknewsclient.domain.usecases.GetPostsCountUseCase
import com.sumin.vknewsclient.domain.usecases.GetProfileInfoUseCase
import com.sumin.vknewsclient.domain.usecases.GetProfilePhotosUseCase
import com.sumin.vknewsclient.domain.usecases.GetWallPostsUseCase
import com.sumin.vknewsclient.domain.usecases.IgnoreItemUseCase
import com.sumin.vknewsclient.domain.usecases.LoadNextDataUseCase
import com.sumin.vknewsclient.domain.usecases.ProfileUseCases
import com.sumin.vknewsclient.presentation.profile.state.FriendsState
import com.sumin.vknewsclient.presentation.profile.state.PhotosState
import com.sumin.vknewsclient.presentation.profile.state.PostsState
import com.sumin.vknewsclient.presentation.profile.state.ProfileScreenState
import com.sumin.vknewsclient.presentation.profile.state.ProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val useCases: ProfileUseCases
): ViewModel() {

    private val _profileState = mutableStateOf(ProfileScreenState.Initial as ProfileScreenState)
    val profileState: State<ProfileScreenState> = _profileState

    private val _postsState = mutableStateOf(PostsState.Initial as PostsState)
    val postsState: State<PostsState> = _postsState


    val postsCountState = useCases.getPostCountUseCase()

    init {
        viewModelScope.launch {
            _postsState.value = PostsState.Loading

        }
    }


    private fun loadProfile(): Flow<ProfileScreenState> {
        _profileState.value = ProfileScreenState.Loading
        return combine(
            useCases.getProfileInfoUseCase().filterNotNull(),
            useCases.getProfilePhotosUseCase().filterNotNull(),
            useCases.getFriendsForProfileUseCase().filterNotNull()
        ){ user, photos, friends ->
            val profileState = ProfileState.Profile(user)

            val photosState = if (photos.isNotEmpty()){
                PhotosState.Photos(photos)
            } else {
                PhotosState.NoPhotos
            }

            val friendsState = if (friends.isNotEmpty()){
                FriendsState.Friends(friends)
            } else {
                FriendsState.NoFriends
            }

            ProfileScreenState.ProfileInfo(
                profileState,
                friendsState,
                photosState
            )
        }
    }

    private fun loadPosts(){
        _postsState.value = PostsState.Loading
        return combine(
            useCases.getPostsUseCase()
        )
    }
}