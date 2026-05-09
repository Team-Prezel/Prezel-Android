package com.team.prezel.feature.setting.impl.delete.contract

import com.team.prezel.core.ui.base.UiIntent
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountReasonOption

internal sealed interface DeleteAccountUiIntent : UiIntent {
    data class ToggleNoticeChecked(
        val checked: Boolean,
    ) : DeleteAccountUiIntent

    data object ClickNext : DeleteAccountUiIntent

    data class SelectReason(
        val reason: DeleteAccountReasonOption,
    ) : DeleteAccountUiIntent

    data class ChangeOtherReason(
        val value: String,
    ) : DeleteAccountUiIntent

    data object ClickWithdraw : DeleteAccountUiIntent

    data object DismissDialog : DeleteAccountUiIntent

    data object ConfirmWithdraw : DeleteAccountUiIntent
}
