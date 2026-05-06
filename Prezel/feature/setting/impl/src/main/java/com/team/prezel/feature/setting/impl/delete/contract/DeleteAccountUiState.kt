package com.team.prezel.feature.setting.impl.delete.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountReasonOption
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountStep

@Immutable
internal data class DeleteAccountUiState(
    val step: DeleteAccountStep = DeleteAccountStep.NOTICE,
    val isNoticeChecked: Boolean = false,
    val selectedReason: DeleteAccountReasonOption? = null,
    val otherReasonText: String = "",
    val isConfirmDialogVisible: Boolean = false,
    val isSubmitting: Boolean = false,
) : UiState {
    val isOtherReasonSelected: Boolean
        get() = selectedReason == DeleteAccountReasonOption.OTHER

    val isNextEnabled: Boolean
        get() = isNoticeChecked

    val isWithdrawEnabled: Boolean
        get() = selectedReason != null && !isSubmitting

    val isPrimaryActionEnabled: Boolean
        get() = if (step == DeleteAccountStep.NOTICE) isNextEnabled else isWithdrawEnabled
}
