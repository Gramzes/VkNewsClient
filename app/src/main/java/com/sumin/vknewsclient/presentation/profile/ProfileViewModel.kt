package com.sumin.vknewsclient.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Resource
import com.sumin.vknewsclient.domain.usecases.ProfileUseCases
import com.sumin.vknewsclient.presentation.profile.state.FriendsState
import com.sumin.vknewsclient.presentation.profile.state.PhotosState
import com.sumin.vknewsclient.presentation.profile.state.PostsState
import com.sumin.vknewsclient.presentation.profile.state.ProfileScreenState
import com.sumin.vknewsclient.presentation.profile.state.ProfileState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val useCases: ProfileUseCases
): ViewModel() {

    private var isFirstLoading = true
    private val postsStateFlow = useCases.getPostsUseCase()

    val profileState = loadProfile()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = ProfileScreenState.Initial)

    val postsState: StateFlow<PostsState> = loadPosts()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = PostsState.Initial)

    private fun loadProfile(): Flow<ProfileScreenState> {
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

            val friendsState = if (friends.isNotEmpty() && user.friendsCount != null){
                FriendsState.Friends(user.friendsCount, friends)
            } else {
                FriendsState.NoFriends
            }

            ProfileScreenState.ProfileInfo(
                profileState,
                friendsState,
                photosState
            ) as ProfileScreenState
        }.onStart {
            emit(ProfileScreenState.Loading)
        }
    }

    private fun loadPosts() = postsStateFlow
        .combine(
            useCases
            .getPostCountUseCase()
            .filterNotNull()
        ){ posts, postsCount ->
            when (posts){
                is Resource.Data -> {
                    PostsState.Posts(postsCount, posts.data)
                }
                is Resource.EndOfData -> {
                    PostsState.Posts(postsCount, posts.data, endOfData = true)
                }
                is Resource.Loading -> {
                    if (isFirstLoading){
                        PostsState.Loading
                    } else {
                        PostsState.Posts(postsCount, posts.data, isNextLoading = true)
                    }
                }
            }
        }
        .onEach { isFirstLoading = false }
        .onStart { emit(PostsState.Loading) }

    fun loadNextPosts(){
        viewModelScope.launch {
            useCases.loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost){
        val changeLikeExceptionHandler = CoroutineExceptionHandler { _, throwable -> }
        viewModelScope.launch(changeLikeExceptionHandler) {
            useCases.changeLikeStatusUseCase(feedPost)
        }
    }
}