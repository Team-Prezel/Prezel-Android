package com.team.prezel.feature.login.impl.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.prezel.core.data.auth.KakaoLoginManager
import com.team.prezel.core.data.auth.KakaoLoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginManager: KakaoLoginManager,
) : ViewModel() {
    private var isLoginInProgress = false

    private val _uiEffect = Channel<LoginUiEffect>()
    val uiEffect: Flow<LoginUiEffect> = _uiEffect.receiveAsFlow()

    fun onClickLogin(
        context: Context,
        failureMessage: String,
    ) {
        if (isLoginInProgress) return

        viewModelScope.launch {
            isLoginInProgress = true

            when (kakaoLoginManager.login(context)) {
                is KakaoLoginResult.Success -> {
                    isLoginInProgress = false
                    _uiEffect.send(LoginUiEffect.NavigateToHome)
                }

                is KakaoLoginResult.Failure -> {
                    isLoginInProgress = false
                    _uiEffect.send(LoginUiEffect.ShowSnackbar(failureMessage))
                }
            }
        }
    }
}
