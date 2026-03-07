package com.team.prezel.feature.splash.impl.viewModel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class SplashViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiEffect = Channel<SplashUiEffect>()
        val uiEffect: Flow<SplashUiEffect> = _uiEffect.receiveAsFlow()

        fun checkLoginStatus() {
            viewModelScope.launch {
                _uiEffect.send(SplashUiEffect.NavigateToLogin)
            }
        }
    }
