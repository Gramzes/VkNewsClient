package com.sumin.vknewsclient.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumin.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.sumin.vknewsclient.domain.usecases.CheckAuthResultUseCase
import com.sumin.vknewsclient.domain.usecases.GetAuthStateUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val checkAuthStateUseCase: CheckAuthResultUseCase
): ViewModel() {

    val authState = getAuthStateUseCase()
    fun performAuthResult(){
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}