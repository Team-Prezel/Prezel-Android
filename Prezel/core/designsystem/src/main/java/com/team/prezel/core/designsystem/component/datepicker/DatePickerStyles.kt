package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

@androidx.compose.runtime.Composable
internal fun dayTextColor(ui: DayCellUiModel): Color =
    when {
        ui.isSelected -> PrezelTheme.colors.bgRegular
        ui.isToday -> PrezelTheme.colors.interactiveRegular
        ui.isSunday -> PrezelTheme.colors.accentMagentaRegular
        else -> PrezelTheme.colors.textMedium
    }
