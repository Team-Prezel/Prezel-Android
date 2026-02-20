package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun dayTextColor(ui: DayCellUiModel): Color =
    when {
        !ui.enabled -> PrezelTheme.colors.textDisabled
        ui.isSelected -> PrezelTheme.colors.bgRegular
        ui.isToday -> PrezelTheme.colors.interactiveRegular
        ui.isSunday -> PrezelTheme.colors.accentMagentaRegular
        else -> PrezelTheme.colors.textMedium
    }
