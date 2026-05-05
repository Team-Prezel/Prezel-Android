package com.team.prezel.core.designsystem.component.datepicker.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.datetime.LocalDate

@Composable
internal fun DayCell(
    dayCell: DayCellType?,
    config: DatePickerDefault,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelTouchArea(
        enabled = dayCell != null && dayCell !is DayCellType.Past,
        modifier = modifier
            .aspectRatio(1f)
            .padding(PrezelTheme.spacing.V4)
            .clip(PrezelTheme.shapes.V1000)
            .background(color = config.dayContainerColor(dayCell = dayCell)),
        isUseRipple = false,
        onClick = onClick,
    ) {
        if (dayCell == null) return@PrezelTouchArea

        Text(
            text = dayCell.date.day.toString(),
            color = config.dayTextColor(dayCell = dayCell),
            style = config.dayTextStyle(dayCell = dayCell),
        )
    }
}

@BasicPreview
@Composable
private fun DayCellPreview() {
    PreviewSection(
        title = "DatePicker/Day",
        description = "DatePicker에 사용되는 리소스입니다.",
    ) {
        PreviewValueRow(name = "Default") {
            Box(modifier = Modifier.size(50.dp)) {
                DayCell(
                    dayCell = DayCellType.Default(LocalDate(year = 2026, month = 2, day = 26), isSelected = false),
                    config = DatePickerDefaults.default(),
                    onClick = {},
                )
            }
        }

        PreviewValueRow(name = "Past") {
            Box(modifier = Modifier.size(50.dp)) {
                DayCell(
                    dayCell = DayCellType.Past(LocalDate(year = 2026, month = 2, day = 26), isSelected = false),
                    config = DatePickerDefaults.default(),
                    onClick = {},
                )
            }
        }

        PreviewValueRow(name = "Today") {
            Box(modifier = Modifier.size(50.dp)) {
                DayCell(
                    dayCell = DayCellType.Today(LocalDate(year = 2026, month = 2, day = 26), isSelected = false),
                    config = DatePickerDefaults.default(),
                    onClick = {},
                )
            }
        }

        PreviewValueRow(name = "Holiday") {
            Box(modifier = Modifier.size(50.dp)) {
                DayCell(
                    dayCell = DayCellType.Holiday(LocalDate(year = 2026, month = 2, day = 26), isSelected = false),
                    config = DatePickerDefaults.default(),
                    onClick = {},
                )
            }
        }

        PreviewValueRow(name = "Selected") {
            Box(modifier = Modifier.size(50.dp)) {
                DayCell(
                    dayCell = DayCellType.Default(LocalDate(year = 2026, month = 2, day = 26), isSelected = true),
                    config = DatePickerDefaults.default(),
                    onClick = {},
                )
            }
        }
    }
}
