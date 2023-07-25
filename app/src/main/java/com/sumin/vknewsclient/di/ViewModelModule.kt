package com.sumin.vknewsclient.di

import androidx.lifecycle.ViewModel
import com.sumin.vknewsclient.presentation.comments.CommentsViewModel
import com.sumin.vknewsclient.presentation.main.MainScreenViewModel
import com.sumin.vknewsclient.presentation.newsfeed.NewsFeedViewModel
import com.sumin.vknewsclient.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    @Binds
    fun bindMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Binds
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    @Binds
    fun bindNewsFeedViewModel(viewModel: NewsFeedViewModel): ViewModel
}