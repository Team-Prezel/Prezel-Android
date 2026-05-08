package com.team.prezel.feature.setting.impl.delete.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountUiMessage

internal sealed interface DeleteAccountUiEffect : UiEffect {
    data object NavigateToSplash : DeleteAccountUiEffect

    data class ShowMessage(
        val message: DeleteAccountUiMessage,
    ) : DeleteAccountUiEffect
}
