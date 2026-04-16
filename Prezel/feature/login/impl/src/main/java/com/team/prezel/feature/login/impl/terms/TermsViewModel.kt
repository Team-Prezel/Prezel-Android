package com.team.prezel.feature.login.impl.terms

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.login.impl.terms.contract.TermsUiEffect
import com.team.prezel.feature.login.impl.terms.contract.TermsUiIntent
import com.team.prezel.feature.login.impl.terms.contract.TermsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TermsViewModel @Inject constructor() : BaseViewModel<TermsUiState, TermsUiIntent, TermsUiEffect>(TermsUiState()) {
    override fun onIntent(intent: TermsUiIntent) {
        when (intent) {
            TermsUiIntent.ToggleAll -> toggleAll()
            TermsUiIntent.ToggleTermsOfService -> toggleTermsOfService()
            TermsUiIntent.TogglePrivacyPolicy -> togglePrivacyPolicy()
            TermsUiIntent.ToggleMarketingConsent -> toggleMarketingConsent()
            TermsUiIntent.ClickContinue -> handleClickContinue()
        }
    }

    private fun toggleAll() {
        val newChecked = !currentState.isAllChecked

        updateState {
            copy(
                isTermsOfServiceChecked = newChecked,
                isPrivacyPolicyChecked = newChecked,
                isMarketingConsentChecked = newChecked,
            )
        }
    }

    private fun toggleTermsOfService() {
        updateState { copy(isTermsOfServiceChecked = !isTermsOfServiceChecked) }
    }

    private fun togglePrivacyPolicy() {
        updateState { copy(isPrivacyPolicyChecked = !isPrivacyPolicyChecked) }
    }

    private fun toggleMarketingConsent() {
        updateState { copy(isMarketingConsentChecked = !isMarketingConsentChecked) }
    }

    private fun handleClickContinue() {
        if (!currentState.isRequiredChecked) return
        viewModelScope.launch {
            sendEffect(TermsUiEffect.NavigateToProfile)
        }
    }
}
