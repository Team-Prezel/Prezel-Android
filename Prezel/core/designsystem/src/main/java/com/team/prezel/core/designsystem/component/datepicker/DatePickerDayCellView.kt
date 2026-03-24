package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewRow
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.datetime.LocalDate

@Composable
internal fun RowScope.DayCellView(
    uiModel: DayCell?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(PrezelTheme.spacing.V4)
            .clip(CircleShape)
            .background(
                if (uiModel?.isSelected == true) {
                    PrezelTheme.colors.interactiveRegular
                } else {
                    Color.Transparent
                },
            ).clickable(
                indication = ripple(),
                interactionSource = null,
                enabled = uiModel?.isVisible == true,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (uiModel == null) return@Box
        if (!uiModel.isVisible) return@Box

        Text(
            text = uiModel.dayText,
            color = uiModel.dayTextColor(),
            style = if (uiModel.isSelected) {
                PrezelTheme.typography.body3Bold
            } else {
                PrezelTheme.typography.body3Medium
            },
        )
    }
}

@BasicPreview
@Composable
private fun DayCellViewPreview() {
    PreviewSection(
        title = "DatePicker/Day",
        description = "DatePicker에 사용되는 리소스입니다.",
    ) {
        PreviewRow {
            DayCellView(
                uiModel = DayCell(
                    date = LocalDate(2024, 1, 1),
                    isSelected = false,
                    isToday = false,
                    isVisible = true,
                ),
                onClick = {},
            )
            DayCellView(
                uiModel = DayCell(
                    date = LocalDate(2024, 1, 2),
                    isSelected = true,
                    isToday = false,
                    isVisible = true,
                ),
                onClick = {},
            )
            DayCellView(
                uiModel = DayCell(
                    date = LocalDate(2024, 1, 3),
                    isSelected = false,
                    isToday = true,
                    isVisible = true,
                ),
                onClick = {},
            )
        }
    }
}
