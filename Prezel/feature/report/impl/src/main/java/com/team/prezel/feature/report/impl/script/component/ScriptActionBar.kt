package com.team.prezel.feature.report.impl.script.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R

@Composable
internal fun ScriptActionBar(
    isApplyAllEnabled: Boolean,
    isCopyEnabled: Boolean,
    onCopyClick: () -> Unit,
    onApplyAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelButtonArea(
        modifier = modifier.fillMaxWidth(),
        isVertical = false,
        isStrongStrength = true,
        mainButton = { modifier ->
            PrezelButton(
                text = stringResource(R.string.feature_report_impl_script_apply_all),
                modifier = modifier,
                enabled = isApplyAllEnabled,
                onClick = onApplyAllClick,
            )
        },
        subButton = { modifier ->
            PrezelButton(
                text = stringResource(R.string.feature_report_impl_script_copy_all),
                modifier = modifier,
                enabled = isCopyEnabled,
                type = ButtonType.GHOST,
                hierarchy = ButtonHierarchy.SECONDARY,
                onClick = onCopyClick,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ScriptActionBarPreview() {
    PrezelTheme {
        ScriptActionBar(
            isApplyAllEnabled = true,
            isCopyEnabled = true,
            onCopyClick = {},
            onApplyAllClick = {},
        )
    }
}
