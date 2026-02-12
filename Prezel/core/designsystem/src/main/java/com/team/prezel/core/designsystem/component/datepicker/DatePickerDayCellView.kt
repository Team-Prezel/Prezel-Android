package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun RowScope.DayCellView(
    uiModel: DayCellUiModel?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(6.dp)
            .clip(CircleShape)
            .background(
                if (uiModel?.isSelected == true) {
                    PrezelTheme.colors.interactiveRegular
                } else {
                    Color.Transparent
                },
            ).clickable(
                enabled = uiModel?.enabled == true,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (uiModel != null) {
            Text(
                text = uiModel.text,
                color = dayTextColor(uiModel),
                style = if (uiModel.isSelected) {
                    PrezelTheme.typography.body3Bold
                } else {
                    PrezelTheme.typography.body3Medium
                },
            )
        }
    }
}
