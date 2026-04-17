package com.team.prezel.feature.splash.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.CheckLoginStatusUseCase
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.splash.impl.contract.SplashUiEffect
import com.team.prezel.feature.splash.impl.contract.SplashUiIntent
import com.team.prezel.feature.splash.impl.contract.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
) : BaseViewModel<SplashUiState, SplashUiIntent, SplashUiEffect>(
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
                if (checkLoginStatusUseCase()) {
                    sendEffect(SplashUiEffect.NavigateToHome)
                } else {
                    sendEffect(SplashUiEffect.NavigateToLogin)
                }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }
}
