package com.team.prezel.feature.splash.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.splash.impl.contract.SplashUiEffect
import com.team.prezel.feature.splash.impl.contract.SplashUiIntent
import com.team.prezel.feature.splash.impl.contract.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor() :
    BaseViewModel<SplashUiState, SplashUiIntent, SplashUiEffect>(
        SplashUiState(),
    ) {
        override fun onIntent(intent: SplashUiIntent) {
            when (intent) {
                SplashUiIntent.CheckLoginStatus -> checkLoginStatus()
            }
        }

        private fun checkLoginStatus() {
            updateState { copy(isLoading = true) }

            viewModelScope
                .launch {
                    sendEffect(SplashUiEffect.NavigateToLogin)
                }.invokeOnCompletion { updateState { copy(isLoading = false) } }
        }
    }
