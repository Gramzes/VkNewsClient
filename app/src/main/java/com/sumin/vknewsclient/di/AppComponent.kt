package com.sumin.vknewsclient.di

import android.app.Application
import com.sumin.vknewsclient.di.comments.CommentScreenComponent
import com.sumin.vknewsclient.di.scopes.ApplicationScope
import com.sumin.vknewsclient.presentation.ViewModelFactory
import com.sumin.vknewsclient.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [DataModule::class, ViewModelModule::class]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun getViewModelFactory(): ViewModelFactory

    fun getCommentScreenComponentFactory(): CommentScreenComponent.Factory


    @Component.Factory
    interface Factory{

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}