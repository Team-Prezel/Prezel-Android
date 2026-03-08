package com.team.prezel.core.designsystem.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

fun Modifier.drawDashBorder(
    color: Color = Color.Gray,
    width: Float = 1f,
    interval: Float = 10f,
    phase: Float = 0f,
) = this.drawWithCache {
    onDrawBehind {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(interval, interval), phase)

        drawRoundRect(
            color = color,
            style = Stroke(
                width = width,
                pathEffect = pathEffect,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun DrawDashBorderPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(12.dp)
                .drawDashBorder(),
        )
    }
}
