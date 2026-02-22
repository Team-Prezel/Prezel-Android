package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun DayCellUiModel.dayTextColor(): Color =
    when {
        !this.enabled -> PrezelTheme.colors.textDisabled
        this.isSelected -> PrezelTheme.colors.bgRegular
        this.isToday -> PrezelTheme.colors.interactiveRegular
        this.isSunday -> PrezelTheme.colors.accentMagentaRegular
        else -> PrezelTheme.colors.textMedium
    }
