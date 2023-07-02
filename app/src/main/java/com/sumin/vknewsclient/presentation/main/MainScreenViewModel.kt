package com.sumin.vknewsclient.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.domain.usecases.CheckAuthResultUseCase
import com.sumin.vknewsclient.domain.usecases.GetAuthStateUseCase
import kotlinx.coroutines.launch

class MainScreenViewModel(application: Application): AndroidViewModel(application) {
    private val repository = NewsFeedRepositoryImpl(application)
    private val getAuthStateUseCase = GetAuthStateUseCase(repository)
    private val checkAuthStateUseCase = CheckAuthResultUseCase(repository)

    val authState = getAuthStateUseCase()

    fun performAuthResult(){
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}