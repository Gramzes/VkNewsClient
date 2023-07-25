package com.sumin.vknewsclient.di.comments

import androidx.lifecycle.ViewModel
import com.sumin.vknewsclient.di.ViewModelKey
import com.sumin.vknewsclient.presentation.comments.CommentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CommentViewModelModule {

    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    @Binds
    fun bindCommentsViewModel(viewModel: CommentsViewModel): ViewModel
}