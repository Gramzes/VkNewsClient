package com.sumin.vknewsclient.di

import android.app.Application
import com.sumin.vknewsclient.data.network.ApiFactory
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.data.repository.ProfileRepositoryImpl
import com.sumin.vknewsclient.data.repository.wall_repositories.NewsFeedWallRepositoryImpl
import com.sumin.vknewsclient.data.repository.wall_repositories.UserProfileWallRepositoryImpl
import com.sumin.vknewsclient.di.qualifiers.NewsFeed
import com.sumin.vknewsclient.di.qualifiers.UserProfile
import com.sumin.vknewsclient.di.scopes.ActivityScope
import com.sumin.vknewsclient.di.scopes.ApplicationScope
import com.sumin.vknewsclient.domain.repository.GetPostsCountRepository
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.domain.repository.ProfileRepository
import com.sumin.vknewsclient.domain.repository.WallRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    @ApplicationScope
    @Binds
    fun bindUserRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @UserProfile
    @ApplicationScope
    @Binds
    fun bindProfileWallRepository(impl: UserProfileWallRepositoryImpl): WallRepository

    @NewsFeed
    @ApplicationScope
    @Binds
    fun bindNewsFeedWallRepository(impl: NewsFeedWallRepositoryImpl): WallRepository

    @UserProfile
    @ApplicationScope
    @Binds
    fun bindGetPostsCountRepository(impl: UserProfileWallRepositoryImpl): GetPostsCountRepository

    companion object{

        @ApplicationScope
        @Provides
        fun provideVkKeyStorage(application: Application): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(application)
        }

        @ApplicationScope
        @Provides
        fun provideVkService(): ApiService{
            return ApiFactory.vkService
        }
    }
}