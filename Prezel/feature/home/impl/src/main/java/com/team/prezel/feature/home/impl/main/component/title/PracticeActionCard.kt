package com.team.prezel.feature.home.impl.main.component.title

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PracticeActionCard(
    title: String,
    actionText: String,
    titleColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = PrezelTheme.shapes.V8)
            .border(
                width = PrezelTheme.stroke.V1,
                shape = PrezelTheme.shapes.V8,
                color = PrezelTheme.colors.borderSmall,
            ).background(color = PrezelTheme.colors.bgRegular)
            .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V12),
    ) {
        Text(
            text = title,
            color = titleColor,
            style = PrezelTheme.typography.body2Bold,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))

        PrezelTouchArea(
            onClick = onClick,
            shape = PrezelTheme.shapes.V4,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = actionText,
                    color = PrezelTheme.colors.textRegular,
                    style = PrezelTheme.typography.caption1Regular,
                )

                Icon(
                    painter = painterResource(PrezelIcons.ChevronRight),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = PrezelTheme.colors.iconRegular,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PracticeActionCardPreview() {
    PrezelTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            PracticeActionCard(
                title = "연습하기",
                actionText = "지금 바로 시작하기",
                titleColor = PrezelTheme.colors.interactiveRegular,
                onClick = {},
            )
        }
    }
}
