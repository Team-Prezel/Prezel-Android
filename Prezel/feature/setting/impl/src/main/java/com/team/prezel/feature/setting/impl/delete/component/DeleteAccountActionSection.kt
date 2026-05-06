package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.advancedImePadding
import com.team.prezel.feature.setting.impl.R
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountStep

@Composable
internal fun DeleteAccountActionSection(
    step: DeleteAccountStep,
    enabled: Boolean,
    onClickNext: () -> Unit,
    onClickWithdraw: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelButtonArea(
        isVertical = false,
        isStrongStrength = true,
        isNested = false,
        showBackground = isShowBackground(),
        mainButton = { modifier ->
            PrezelTextButton(
                modifier = modifier,
                text = step.buttonText(),
                enabled = enabled,
                onClick = if (step == DeleteAccountStep.NOTICE) onClickNext else onClickWithdraw,
            )
        },
        modifier = modifier.advancedImePadding(),
    )
}

@Composable
private fun isShowBackground(): Boolean {
    val density = LocalDensity.current
    return WindowInsets.ime.getBottom(density) > 0
}

@Composable
private fun DeleteAccountStep.buttonText(): String =
    when (this) {
        DeleteAccountStep.NOTICE -> R.string.feature_setting_impl_delete_account_next
        DeleteAccountStep.REASON -> R.string.feature_setting_impl_delete_account_withdraw
    }.let { resId -> stringResource(resId) }

@BasicPreview
@Composable
private fun DeleteAccountActionSectionNoticePreview() {
    PrezelTheme {
        DeleteAccountActionSection(
            step = DeleteAccountStep.NOTICE,
            enabled = true,
            onClickNext = {},
            onClickWithdraw = {},
        )
    }
}
