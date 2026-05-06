package com.team.prezel.feature.setting.impl.delete

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.auth.WithdrawUseCase
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiEffect
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiIntent
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiState
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountReasonOption
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountStep
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class DeleteAccountViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
) : BaseViewModel<DeleteAccountUiState, DeleteAccountUiIntent, DeleteAccountUiEffect>(DeleteAccountUiState()) {
    override fun onIntent(intent: DeleteAccountUiIntent) {
        when (intent) {
            DeleteAccountUiIntent.ClickNext -> moveToReasonStep()
            DeleteAccountUiIntent.ClickWithdraw -> showConfirmDialog()
            DeleteAccountUiIntent.ConfirmWithdraw -> withdraw()
            DeleteAccountUiIntent.DismissDialog -> dismissDialog()
            is DeleteAccountUiIntent.ChangeOtherReason -> updateState { copy(otherReasonText = intent.value) }
            is DeleteAccountUiIntent.SelectReason -> selectReason(intent.reason)
            is DeleteAccountUiIntent.ToggleNoticeChecked -> updateState { copy(isNoticeChecked = intent.checked) }
        }
    }

    private fun moveToReasonStep() {
        if (currentState.isNextEnabled) {
            updateState { copy(step = DeleteAccountStep.REASON) }
        }
    }

    private fun selectReason(reason: DeleteAccountReasonOption) {
        updateState {
            copy(
                selectedReason = reason,
                otherReasonText = if (reason == DeleteAccountReasonOption.OTHER) otherReasonText else "",
            )
        }
    }

    private fun showConfirmDialog() {
        if (currentState.isWithdrawEnabled) {
            updateState { copy(isConfirmDialogVisible = true) }
        }
    }

    private fun dismissDialog() {
        updateState { copy(isConfirmDialogVisible = false) }
    }

    private fun withdraw() {
        val reason = currentState.toWithdrawReason() ?: return

        updateState {
            copy(
                isConfirmDialogVisible = false,
                isSubmitting = true,
            )
        }

        viewModelScope.launch {
            withdrawUseCase(reason)
                .onSuccess {
                    sendEffect(DeleteAccountUiEffect.NavigateToSplash)
                }.onFailure { exception ->
                    Timber.e(t = exception)
                    updateState { copy(isSubmitting = false) }
                    sendEffect(DeleteAccountUiEffect.ShowMessage(DeleteAccountUiMessage.WITHDRAW_FAILED))
                }
        }
    }

    private fun DeleteAccountUiState.toWithdrawReason(): WithdrawReason? =
        when (selectedReason) {
            DeleteAccountReasonOption.NOT_USED_OFTEN -> WithdrawReason.NotUsedOften
            DeleteAccountReasonOption.NO_LONGER_NEEDED -> WithdrawReason.NoLongerNeeded
            DeleteAccountReasonOption.TOO_DIFFICULT_OR_COMPLEX -> WithdrawReason.TooDifficultOrComplex
            DeleteAccountReasonOption.ANALYSIS_RESULT_INACCURATE -> WithdrawReason.AnalysisResultInaccurate
            DeleteAccountReasonOption.TOO_MANY_ERRORS -> WithdrawReason.TooManyErrors
            DeleteAccountReasonOption.OTHER -> WithdrawReason.Other(text = otherReasonText.trim())

            null -> null
        }
}
