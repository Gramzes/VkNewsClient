package com.sumin.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(feedPost, it) as CommentsScreenState
        }
        .onStart { emit(CommentsScreenState.Loading) }

}
