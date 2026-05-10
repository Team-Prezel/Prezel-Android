package com.team.prezel.feature.setting.impl.setting.component

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
internal fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit,
) {
    PrezelDialog(
        title = stringResource(R.string.feature_setting_impl_logout_dialog_title),
        onDismiss = onDismiss,
    ) {
        Action(label = stringResource(R.string.feature_setting_impl_cancel)) {
            onDismiss()
        }
        Action(
            label = stringResource(R.string.feature_setting_impl_logout),
            type = ActionType.BAD,
        ) {
            onConfirmLogout()
        }
    }
}

@BasicPreview
@Composable
private fun LogoutDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LogoutDialog(
                onDismiss = {},
                onConfirmLogout = {},
            )
        }
    }
}
