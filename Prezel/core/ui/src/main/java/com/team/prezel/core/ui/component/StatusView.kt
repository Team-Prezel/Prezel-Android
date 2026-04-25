package com.team.prezel.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun StatusView(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    visual: @Composable (() -> Unit)? = null,
    action: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        visual?.invoke()

        Column(
            modifier = Modifier.padding(top = PrezelTheme.spacing.V16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textMedium,
                textAlign = TextAlign.Center,
            )

            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    modifier = Modifier.padding(top = PrezelTheme.spacing.V4),
                    style = PrezelTheme.typography.caption2Regular,
                    color = PrezelTheme.colors.textSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }

        if (action != null) {
            Box(
                modifier = Modifier.padding(top = PrezelTheme.spacing.V16),
                contentAlignment = Alignment.Center,
            ) {
                action()
            }
        }
    }
}

@BasicPreview
@Composable
private fun StatusViewEmptyPreview() {
    PrezelTheme {
        StatusView(
            title = "아직 준비중인 발표가 없어요",
            visual = {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(color = PrezelTheme.colors.bgMedium),
                )
            },
            action = {
                PrezelButton(
                    text = "발표 추가하기",
                    iconResId = PrezelIcons.Blank,
                    type = ButtonType.OUTLINED,
                    size = ButtonSize.SMALL,
                    isRounded = true,
                    onClick = { },
                )
            },
        )
    }
}

@BasicPreview
@Composable
private fun StatusViewErrorPreview() {
    PrezelTheme {
        StatusView(
            title = "문제가 발생했어요",
            description = "음성이 작거나 주변 소음이 많았을 수 있어요.\n조용한 환경에서 다시 녹음해 주세요.",
            visual = {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(color = PrezelTheme.colors.bgMedium),
                )
            },
            action = {
                PrezelButton(
                    text = "다시 시도하기",
                    iconResId = PrezelIcons.Blank,
                    type = ButtonType.FILLED,
                    size = ButtonSize.SMALL,
                    hierarchy = ButtonHierarchy.SECONDARY,
                    isRounded = true,
                    onClick = { },
                )
            },
        )
    }
}
