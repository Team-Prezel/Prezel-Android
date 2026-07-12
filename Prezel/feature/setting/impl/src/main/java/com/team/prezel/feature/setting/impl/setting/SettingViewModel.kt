package com.team.prezel.feature.setting.impl.setting

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.auth.LogoutUseCase
import com.team.prezel.core.domain.usecase.terms.FetchTermsUseCase
import com.team.prezel.core.domain.usecase.user.FetchUserInfoUseCase
import com.team.prezel.core.model.terms.Term
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiEffect
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiIntent
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiState
import com.team.prezel.feature.setting.impl.setting.model.SettingUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val fetchTermsUseCase: FetchTermsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<SettingUiState, SettingUiIntent, SettingUiEffect>(SettingUiState()) {
    override fun onIntent(intent: SettingUiIntent) {
        when (intent) {
            SettingUiIntent.FetchData -> fetchData()
            SettingUiIntent.ClickTermsOfService -> navigateToTermsDetail(TermsCategory.TERMS_OF_SERVICE)
            SettingUiIntent.ClickPrivacyPolicy -> navigateToTermsDetail(TermsCategory.PRIVACY_POLICY)
            SettingUiIntent.ClickLogout -> logout()
        }
    }

    private fun fetchData() {
        updateState { copy(isLoading = true) }

        viewModelScope
            .launch {
                fetchUserInfo()
                fetchTerms()
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private suspend fun fetchUserInfo() {
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
    }

    private suspend fun fetchTerms() {
        fetchTermsUseCase()
            .onSuccess { terms ->
                updateState { copy(terms = terms.toImmutableList()) }
            }.onFailure { exception ->
                Timber.e(t = exception)
            }
    }

    private fun navigateToTermsDetail(category: TermsCategory) {
        val term = currentState.terms.firstOrNull { it.matches(category) } ?: return

        viewModelScope.launch {
            sendEffect(
                SettingUiEffect.NavigateToTermsDetail(
                    title = term.title,
                    url = term.content,
                ),
            )
        }
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

    private fun Term.matches(category: TermsCategory): Boolean =
        when (category) {
            TermsCategory.TERMS_OF_SERVICE -> title.contains("이용약관")
            TermsCategory.PRIVACY_POLICY -> title.contains("개인정보")
        }

    private enum class TermsCategory {
        TERMS_OF_SERVICE,
        PRIVACY_POLICY,
    }
}
