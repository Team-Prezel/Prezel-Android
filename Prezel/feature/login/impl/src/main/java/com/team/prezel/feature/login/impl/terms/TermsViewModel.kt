package com.team.prezel.feature.login.impl.terms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.prezel.feature.login.impl.terms.contract.TermsUiEffect
import com.team.prezel.feature.login.impl.terms.contract.TermsUiIntent
import com.team.prezel.feature.login.impl.terms.contract.TermsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TermsViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(TermsUiState())
        val uiState: StateFlow<TermsUiState> = _uiState
        private val currentState: TermsUiState
            get() = uiState.value

        private val _uiEffect = Channel<TermsUiEffect>()
        val uiEffect: Flow<TermsUiEffect> = _uiEffect.receiveAsFlow()

        fun onIntent(intent: TermsUiIntent) {
            when (intent) {
                TermsUiIntent.ToggleAll -> toggleAll()
                TermsUiIntent.ToggleTermsOfService -> toggleTermsOfService()
                TermsUiIntent.TogglePrivacyPolicy -> togglePrivacyPolicy()
                TermsUiIntent.ToggleMarketingConsent -> toggleMarketingConsent()
                TermsUiIntent.ClickContinue -> handleClickContinue()
            }
        }

        private fun update(reducer: TermsUiState.() -> TermsUiState) {
            _uiState.update(reducer)
        }

        private fun toggleAll() {
            val newChecked = !currentState.isAllChecked

            update {
                copy(
                    isTermsOfServiceChecked = newChecked,
                    isPrivacyPolicyChecked = newChecked,
                    isMarketingConsentChecked = newChecked,
                )
            }
        }

        private fun toggleTermsOfService() {
            update { copy(isTermsOfServiceChecked = !isTermsOfServiceChecked) }
        }

        private fun togglePrivacyPolicy() {
            update { copy(isPrivacyPolicyChecked = !isPrivacyPolicyChecked) }
        }

        private fun toggleMarketingConsent() {
            update { copy(isMarketingConsentChecked = !isMarketingConsentChecked) }
        }

        private fun handleClickContinue() {
            if (!currentState.isRequiredChecked) return
            viewModelScope.launch {
                _uiEffect.send(TermsUiEffect.NavigateToHome)
            }
        }
    }
