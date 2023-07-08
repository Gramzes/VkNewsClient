package com.sumin.vknewsclient.di

import android.app.Application
import com.sumin.vknewsclient.data.network.ApiFactory
import com.sumin.vknewsclient.data.network.ApiService
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.data.repository.ProfileRepositoryImpl
import com.sumin.vknewsclient.domain.repository.NewsFeedRepository
import com.sumin.vknewsclient.domain.repository.ProfileRepository
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
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

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