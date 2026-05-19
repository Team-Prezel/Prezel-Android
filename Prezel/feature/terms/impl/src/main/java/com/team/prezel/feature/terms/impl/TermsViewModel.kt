package com.team.prezel.feature.terms.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.usecase.terms.AgreeTermsUseCase
import com.team.prezel.core.model.terms.TermsAgreement
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.terms.impl.contract.TermsUiEffect
import com.team.prezel.feature.terms.impl.contract.TermsUiIntent
import com.team.prezel.feature.terms.impl.contract.TermsUiState
import com.team.prezel.feature.terms.impl.model.TermsUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TERMS_OF_SERVICE_ID = 1L
private const val PRIVACY_POLICY_ID = 2L
private const val MARKETING_CONSENT_ID = 3L

@HiltViewModel
internal class TermsViewModel @Inject constructor(
    private val agreeTermsUseCase: AgreeTermsUseCase,
) : BaseViewModel<TermsUiState, TermsUiIntent, TermsUiEffect>(TermsUiState()) {
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
        if (!currentState.isRequiredChecked || currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            agreeTermsUseCase(
                terms = listOf(
                    TermsAgreement(termsId = TERMS_OF_SERVICE_ID, isAgreed = currentState.isTermsOfServiceChecked),
                    TermsAgreement(termsId = PRIVACY_POLICY_ID, isAgreed = currentState.isPrivacyPolicyChecked),
                    TermsAgreement(termsId = MARKETING_CONSENT_ID, isAgreed = currentState.isMarketingConsentChecked),
                ),
            ).onSuccess {
                sendEffect(TermsUiEffect.NavigateToProfile)
            }.onFailure { throwable ->
                sendEffect(TermsUiEffect.ShowMessage(message = throwable.toTermsUiMessage()))
            }

            updateState { copy(isLoading = false) }
        }
    }

    private fun Throwable.toTermsUiMessage(): TermsUiMessage {
        val error = (this as? AppException)?.error

        return when (error) {
            AppError.UNAUTHORIZED,
            AppError.INVALID_REQUEST,
            -> TermsUiMessage.AGREE_TERMS_FAILED_INVALID_REQUEST

            AppError.NETWORK -> TermsUiMessage.AGREE_TERMS_FAILED_NETWORK
            AppError.SERVER_ERROR -> TermsUiMessage.AGREE_TERMS_FAILED_SERVER
            AppError.NOT_FOUND,
            AppError.DUPLICATE,
            AppError.VOICE_RECOGNITION_FAILED,
            AppError.UNKNOWN,
            null,
            -> TermsUiMessage.AGREE_TERMS_FAILED_UNKNOWN
        }
    }
}
