package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography

internal object DatePickerDefaults {
    fun dayTextColor(
        uiModel: DayCell,
        colors: PrezelColors,
    ): Color =
        when {
            uiModel.isSelected -> colors.bgRegular
            uiModel.isToday -> colors.interactiveRegular
            uiModel.isSunday -> colors.accentMagentaRegular
            else -> colors.textMedium
        }

    fun dayContainerColor(
        uiModel: DayCell?,
        colors: PrezelColors,
    ): Color =
        if (uiModel?.isSelected == true) {
            colors.interactiveRegular
        } else {
            Color.Transparent
        }

    fun dayTextStyle(
        uiModel: DayCell,
        typography: PrezelTypography,
    ): TextStyle =
        if (uiModel.isSelected) {
            typography.body3Bold
        } else {
            typography.body3Medium
        }
}
