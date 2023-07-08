package com.sumin.vknewsclient.application

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sumin.vknewsclient.di.AppComponent
import com.sumin.vknewsclient.di.DaggerAppComponent
import com.sumin.vknewsclient.domain.model.Comments
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Like
import com.sumin.vknewsclient.domain.model.Share
import com.sumin.vknewsclient.domain.model.Statistics
import com.sumin.vknewsclient.domain.model.Views

class MyApp(): Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }
}

fun Context.component(): AppComponent{
    return when(this){
        is MyApp -> component
        else -> (this.applicationContext as MyApp).component
    }
}

@Composable
fun getComponent(): AppComponent{
    Log.d("TEST","getComponent")
    return LocalContext.current.component()
}