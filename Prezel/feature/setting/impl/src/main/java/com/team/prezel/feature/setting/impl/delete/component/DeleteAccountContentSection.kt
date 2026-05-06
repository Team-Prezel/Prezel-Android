package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiState
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountReasonOption
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountStep

@Composable
internal fun DeleteAccountContentSection(
    uiState: DeleteAccountUiState,
    onToggleNoticeChecked: (Boolean) -> Unit,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        when (uiState.step) {
            DeleteAccountStep.NOTICE -> DeleteAccountNoticeStep(
                isChecked = uiState.isNoticeChecked,
                onCheckedChange = onToggleNoticeChecked,
            )

            DeleteAccountStep.REASON -> DeleteAccountReasonStep(
                selectedReason = uiState.selectedReason,
                otherReasonText = uiState.otherReasonText,
                onSelectReason = onSelectReason,
                onOtherReasonChanged = onOtherReasonChanged,
            )
        }
    }
}

@BasicPreview
@Composable
private fun DeleteAccountContentSectionNoticePreview() {
    PrezelTheme {
        DeleteAccountContentSection(
            uiState = DeleteAccountUiState(
                step = DeleteAccountStep.NOTICE,
            ),
            onToggleNoticeChecked = {},
            onSelectReason = {},
            onOtherReasonChanged = {},
        )
    }
}

@BasicPreview
@Composable
private fun DeleteAccountContentSectionReasonPreview() {
    PrezelTheme {
        DeleteAccountContentSection(
            uiState = DeleteAccountUiState(
                step = DeleteAccountStep.REASON,
                selectedReason = DeleteAccountReasonOption.OTHER,
            ),
            onToggleNoticeChecked = {},
            onSelectReason = {},
            onOtherReasonChanged = {},
        )
    }
}
