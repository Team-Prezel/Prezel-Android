package com.team.prezel.core.designsystem.component.datepicker.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class DatePickerDefault(
    val containerColor: Color,
    private val selectedDayBackgroundColor: Color,
    private val unselectedDayBackgroundColor: Color,
    private val selectedDayTextStyle: TextStyle,
    private val unselectedDayTextStyle: TextStyle,
    private val selectedDayTextColor: Color,
    private val dayTextColor: Color,
    private val pastDayTextColor: Color,
    private val todayDayTextColor: Color,
    private val holidayDayTextColor: Color,
) {
    internal fun dayContainerColor(dayCell: DayCellType?): Color =
        if (dayCell?.isSelected == true) selectedDayBackgroundColor else unselectedDayBackgroundColor

    internal fun dayTextStyle(dayCell: DayCellType): TextStyle = if (dayCell.isSelected) selectedDayTextStyle else unselectedDayTextStyle

    internal fun dayTextColor(dayCell: DayCellType): Color {
        if (dayCell.isSelected) return selectedDayTextColor

        return when (dayCell) {
            is DayCellType.Default -> dayTextColor
            is DayCellType.Past -> pastDayTextColor
            is DayCellType.Today -> todayDayTextColor
            is DayCellType.Holiday -> holidayDayTextColor
        }
    }
}

internal object DatePickerDefaults {
    @Composable
    fun default(): DatePickerDefault =
        DatePickerDefault(
            containerColor = PrezelTheme.colors.bgRegular,
            selectedDayBackgroundColor = PrezelTheme.colors.interactiveRegular,
            unselectedDayBackgroundColor = Color.Transparent,
            selectedDayTextStyle = PrezelTheme.typography.body3Bold,
            unselectedDayTextStyle = PrezelTheme.typography.body3Medium,
            selectedDayTextColor = PrezelColorScheme.Dark.textLarge,
            dayTextColor = PrezelTheme.colors.textMedium,
            pastDayTextColor = Color.Transparent,
            todayDayTextColor = PrezelTheme.colors.interactiveRegular,
            holidayDayTextColor = PrezelTheme.colors.accentMagentaRegular,
        )
}
