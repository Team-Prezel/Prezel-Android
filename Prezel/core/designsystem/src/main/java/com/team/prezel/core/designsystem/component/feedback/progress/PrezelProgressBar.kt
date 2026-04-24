package com.team.prezel.core.designsystem.component.feedback.progress

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelProgressBar(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
    progressColor: Color = PrezelTheme.colors.interactiveRegular,
    trackColor: Color = PrezelTheme.colors.bgDisabled,
) {
    val coercedProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(PrezelTheme.spacing.V4)
            .clip(PrezelTheme.shapes.V1000)
            .background(trackColor)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedProgress,
                    range = 0f..1f,
                )
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(coercedProgress)
                .clip(PrezelTheme.shapes.V1000)
                .background(progressColor),
        )
    }
}

@BasicPreview
@Composable
private fun PrezelProgressBarPreview() {
    PreviewSection(
        title = "Progress Bar",
        description = "Progress Bar는 작업의 진행 상태를 표시합니다.",
    ) {
        PreviewValueRow(name = "Default") {
            PrezelProgressBar(
                progress = 0.6f,
                modifier = Modifier.width(240.dp),
            )
        }
        PreviewValueRow(name = "Custom") {
            PrezelProgressBar(
                progress = 0.6f,
                modifier = Modifier.width(240.dp),
                progressColor = PrezelTheme.colors.accentPurpleRegular,
                trackColor = PrezelTheme.colors.accentPurpleSmall,
            )
        }
    }
}
