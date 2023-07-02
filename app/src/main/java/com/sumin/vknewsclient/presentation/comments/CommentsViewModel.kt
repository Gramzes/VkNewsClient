package com.sumin.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CommentsViewModel(val feedPost: FeedPost, application: Application): AndroidViewModel(application) {

    private val repository = NewsFeedRepositoryImpl(getApplication())
    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(feedPost, it) as CommentsScreenState
        }
        .onStart { emit(CommentsScreenState.Loading) }

    class Factory(
        private val feedPost: FeedPost,
        private val application: Application
        ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CommentsViewModel(feedPost, application) as T
        }
    }
}
