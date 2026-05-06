package com.team.prezel.feature.setting.impl.setting.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.setting.impl.R

@Composable
internal fun SettingActionSection(
    onClickWithdraw: () -> Unit,
    onClickLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelButtonArea(
        modifier = modifier,
        mainButton = { modifier ->
            PrezelTextButton(
                modifier = modifier,
                text = stringResource(R.string.feature_setting_impl_withdraw),
                type = ButtonType.GHOST,
                size = ButtonSize.XSMALL,
                hierarchy = ButtonHierarchy.SECONDARY,
                onClick = onClickWithdraw,
            )
        },
        subButton = { modifier ->
            PrezelTextButton(
                modifier = modifier,
                text = stringResource(R.string.feature_setting_impl_logout),
                type = ButtonType.FILLED,
                size = ButtonSize.SMALL,
                hierarchy = ButtonHierarchy.SECONDARY,
                onClick = onClickLogout,
            )
        },
    )
}

@BasicPreview
@Composable
private fun SettingActionSectionPreview() {
    PrezelTheme {
        SettingActionSection(
            onClickWithdraw = {},
            onClickLogout = {},
        )
    }
}
