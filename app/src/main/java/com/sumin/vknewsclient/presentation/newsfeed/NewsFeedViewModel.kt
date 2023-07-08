package com.sumin.vknewsclient.presentation.newsfeed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.sumin.vknewsclient.domain.usecases.GetNewsFeedUseCase
import com.sumin.vknewsclient.domain.usecases.IgnoreItemUseCase
import com.sumin.vknewsclient.domain.usecases.LoadNextDataUseCase
import com.sumin.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getNewsFeedUseCase: GetNewsFeedUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val ignoreItemUseCase: IgnoreItemUseCase
): ViewModel() {


    private val newsFeedStateFlow = getNewsFeedUseCase()
    private val loadNextDataEvents = MutableSharedFlow<Unit>(replay = 1)
    private val loadNextDataFlow = flow {
        loadNextDataEvents.collect{
            emit(NewsFeedScreenState.Posts(
                posts = newsFeedStateFlow.value,
                isNextLoading = true
            ))
        }
    }

    val newsFeedState = newsFeedStateFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(it) as NewsFeedScreenState}
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextNewsFeed(){
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
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