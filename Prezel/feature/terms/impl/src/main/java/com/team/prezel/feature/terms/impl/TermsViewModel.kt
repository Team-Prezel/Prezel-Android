package com.team.prezel.feature.terms.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.usecase.terms.AgreeTermsUseCase
import com.team.prezel.core.domain.usecase.terms.FetchTermsUseCase
import com.team.prezel.core.model.terms.Term
import com.team.prezel.core.model.terms.TermsAgreement
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.terms.impl.contract.TermsUiEffect
import com.team.prezel.feature.terms.impl.contract.TermsUiIntent
import com.team.prezel.feature.terms.impl.contract.TermsUiState
import com.team.prezel.feature.terms.impl.model.TermsAgreementUiModel
import com.team.prezel.feature.terms.impl.model.TermsUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TermsViewModel @Inject constructor(
    private val fetchTermsUseCase: FetchTermsUseCase,
    private val agreeTermsUseCase: AgreeTermsUseCase,
) : BaseViewModel<TermsUiState, TermsUiIntent, TermsUiEffect>(TermsUiState()) {
    override fun onIntent(intent: TermsUiIntent) {
        when (intent) {
            TermsUiIntent.FetchTerms -> fetchTerms()
            TermsUiIntent.ToggleAll -> toggleAll()
            is TermsUiIntent.ToggleTerm -> toggleTerm(intent.termsId)
            TermsUiIntent.ClickContinue -> handleClickContinue()
        }
    }

    private fun fetchTerms() {
        if (currentState.isTermsLoading) return

        viewModelScope.launch {
            updateState { copy(isTermsLoading = true) }

            fetchTermsUseCase()
                .onSuccess { terms ->
                    updateState {
                        copy(
                            terms = terms.map { term -> term.toUiModel() }.toImmutableList(),
                        )
                    }
                }.onFailure { throwable ->
                    sendEffect(TermsUiEffect.ShowMessage(message = throwable.toFetchTermsUiMessage()))
                }

            updateState { copy(isTermsLoading = false) }
        }
    }

    private fun toggleAll() {
        val newChecked = !currentState.isAllChecked

        updateState {
            copy(
                terms = terms.map { term -> term.copy(isChecked = newChecked) }.toImmutableList(),
            )
        }
    }

    private fun toggleTerm(termsId: Long) {
        updateState {
            copy(
                terms = terms
                    .map { term ->
                        if (term.termsId == termsId) {
                            term.copy(isChecked = !term.isChecked)
                        } else {
                            term
                        }
                    }.toImmutableList(),
            )
        }
    }

    private fun handleClickContinue() {
        if (!currentState.isRequiredChecked || currentState.isLoading || currentState.isTermsLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            agreeTermsUseCase(
                terms = currentState.terms.map { term ->
                    TermsAgreement(termsId = term.termsId, isAgreed = term.isChecked)
                },
            ).onSuccess {
                sendEffect(TermsUiEffect.NavigateToProfile)
            }.onFailure { throwable ->
                sendEffect(TermsUiEffect.ShowMessage(message = throwable.toAgreeTermsUiMessage()))
            }

            updateState { copy(isLoading = false) }
        }
    }

    private fun Throwable.toFetchTermsUiMessage(): TermsUiMessage {
        val error = (this as? AppException)?.error

        return when (error) {
            AppError.NETWORK -> TermsUiMessage.FETCH_TERMS_FAILED_NETWORK
            AppError.SERVER_ERROR -> TermsUiMessage.FETCH_TERMS_FAILED_SERVER
            AppError.UNAUTHORIZED,
            AppError.INVALID_REQUEST,
            AppError.NOT_FOUND,
            AppError.DUPLICATE,
            AppError.UNKNOWN,
            null,
            -> TermsUiMessage.FETCH_TERMS_FAILED_UNKNOWN

            else -> TermsUiMessage.FETCH_TERMS_FAILED_UNKNOWN
        }
    }

    private fun Throwable.toAgreeTermsUiMessage(): TermsUiMessage {
        val error = (this as? AppException)?.error

        return when (error) {
            AppError.UNAUTHORIZED,
            AppError.INVALID_REQUEST,
            -> TermsUiMessage.AGREE_TERMS_FAILED_INVALID_REQUEST

            AppError.NETWORK -> TermsUiMessage.AGREE_TERMS_FAILED_NETWORK
            AppError.SERVER_ERROR -> TermsUiMessage.AGREE_TERMS_FAILED_SERVER
            AppError.NOT_FOUND,
            AppError.DUPLICATE,
            AppError.UNKNOWN,
            null,
            -> TermsUiMessage.AGREE_TERMS_FAILED_UNKNOWN

            else -> TermsUiMessage.AGREE_TERMS_FAILED_UNKNOWN
        }
    }

    private fun Term.toUiModel(): TermsAgreementUiModel =
        TermsAgreementUiModel(
            termsId = termsId,
            title = title,
            summary = summary,
            contentUrl = content,
            isRequired = isRequired,
        )
}
