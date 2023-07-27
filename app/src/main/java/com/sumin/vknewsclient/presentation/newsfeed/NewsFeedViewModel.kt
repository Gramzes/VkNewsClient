package com.sumin.vknewsclient.presentation.newsfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumin.vknewsclient.di.qualifiers.NewsFeed
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Resource
import com.sumin.vknewsclient.domain.repository.WallRepository
import com.sumin.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.sumin.vknewsclient.domain.usecases.GetWallPostsUseCase
import com.sumin.vknewsclient.domain.usecases.IgnoreItemUseCase
import com.sumin.vknewsclient.domain.usecases.LoadNextDataUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    @NewsFeed
    repository: WallRepository,
    changeLikeStatusFactory: ChangeLikeStatusUseCase.Factory,
    getPostsFactory: GetWallPostsUseCase.Factory,
    loadNextDataFactory: LoadNextDataUseCase.Factory,
    ignoreItemFactory: IgnoreItemUseCase.Factory
): ViewModel() {

    private val getPostsUseCase = getPostsFactory.create(repository)
    private val loadNextDataUseCase = loadNextDataFactory.create(repository)
    private val changeLikeStatusUseCase = changeLikeStatusFactory.create(repository)
    private val ignoreItemUseCase = ignoreItemFactory.create(repository)

    private var isFirstLoading = true
    private val newsFeedStateFlow = getPostsUseCase()

    val newsFeedState = newsFeedStateFlow
        .map {
            when (it){
                is Resource.Data -> {
                    NewsFeedScreenState.Posts(it.data)
                }
                is Resource.EndOfData -> NewsFeedScreenState.Posts(it.data, endOfData = true)
                is Resource.Loading -> {
                    if (isFirstLoading){
                        NewsFeedScreenState.Loading
                    } else {
                        NewsFeedScreenState.Posts(it.data, isNextLoading = true)
                    }
                }
            }
        }
        .onEach { isFirstLoading = false }
        .onStart { emit(NewsFeedScreenState.Loading) }

    fun loadNextNewsFeed(){
        viewModelScope.launch {
            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost){
        val changeLikeExceptionHandler = CoroutineExceptionHandler { _, throwable -> }
        viewModelScope.launch(changeLikeExceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun deletePost(feedPost: FeedPost){
        val deletePostExceptionHandler = CoroutineExceptionHandler { _, throwable -> }
        viewModelScope.launch(deletePostExceptionHandler) {
            ignoreItemUseCase(feedPost)
        }
    }
}