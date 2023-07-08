package com.sumin.vknewsclient.di

import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [CommentViewModelModule::class])
interface CommentScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory{

        fun create(@BindsInstance feedPost: FeedPost): CommentScreenComponent
    }
}