package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialog
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialogScope.ActionType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.setting.impl.R

@Composable
internal fun DeleteAccountConfirmDialog(
    onDismissDialog: () -> Unit,
    onConfirmWithdraw: () -> Unit,
) {
    PrezelDialog(
        title = stringResource(R.string.feature_setting_impl_delete_account_dialog_title),
        onDismiss = onDismissDialog,
    ) {
        Action(label = stringResource(R.string.feature_setting_impl_delete_account_dialog_cancel)) { onDismissDialog() }
        Action(
            label = stringResource(R.string.feature_setting_impl_delete_account_dialog_confirm),
            type = ActionType.BAD,
        ) {
            onConfirmWithdraw()
        }
    }
}

@BasicPreview
@Composable
private fun DeleteAccountConfirmDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DeleteAccountConfirmDialog(
                onDismissDialog = {},
                onConfirmWithdraw = {},
            )
        }
    }
}
