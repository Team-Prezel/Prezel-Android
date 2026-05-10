package com.team.prezel.feature.setting.impl.setting

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.auth.LogoutUseCase
import com.team.prezel.core.domain.usecase.user.FetchUserInfoUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiEffect
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiIntent
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiState
import com.team.prezel.feature.setting.impl.setting.model.SettingUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<SettingUiState, SettingUiIntent, SettingUiEffect>(SettingUiState()) {
    override fun onIntent(intent: SettingUiIntent) {
        when (intent) {
            SettingUiIntent.FetchData -> fetchUserInfo()
            SettingUiIntent.ClickLogout -> logout()
        }
    }

    private fun fetchUserInfo() {
        updateState { copy(isLoading = true) }

        viewModelScope
            .launch {
                fetchUserInfoUseCase()
                    .onSuccess { user ->
                        updateState {
                            copy(
                                profileImageUrl = user.profileImageUrl,
                                nickname = user.nickname,
                                email = user.email,
                            )
                        }
                    }.onFailure { exception ->
                        Timber.e(t = exception)
                        sendEffect(SettingUiEffect.ShowMessage(SettingUiMessage.FETCH_USER_INFO_FAILED))
                    }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
                .onSuccess {
                    sendEffect(SettingUiEffect.NavigateToSplash)
                }.onFailure { exception ->
                    Timber.e(t = exception)
                    sendEffect(SettingUiEffect.ShowMessage(SettingUiMessage.LOGOUT_FAILED))
                }
        }
    }
}
