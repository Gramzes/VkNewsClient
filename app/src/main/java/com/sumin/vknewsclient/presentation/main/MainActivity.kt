package com.sumin.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sumin.vknewsclient.application.component
import com.sumin.vknewsclient.application.getComponent
import com.sumin.vknewsclient.domain.model.AuthState
import com.sumin.vknewsclient.presentation.ViewModelFactory
import com.sumin.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        component().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            val component = getComponent()
            val viewModel: MainScreenViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(AuthState.Initial)

            VkNewsClientTheme {
                when(authState.value){
                    AuthState.Authorized -> {
                        MainScreen()
                    }
                    AuthState.NotAuthorized -> {
                        val authLauncher = rememberLauncherForActivityResult(
                            VK.getVKAuthActivityResultContract()) {
                            viewModel.performAuthResult()
                        }
                        LoginScreen {
                            authLauncher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                        }
                    }
                    AuthState.Initial -> {

                    }
                }
            }
        }
    }
}
