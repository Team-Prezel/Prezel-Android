package com.team.prezel.core.designsystem.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 점선 테두리를 그리는 [Modifier] 확장 함수.
 *
 * [shape] 형태의 외곽선을 기준으로 점선(border)을 렌더링한다.
 *
 * @param color 테두리 색상
 * @param shape 테두리 모양
 * @param width 테두리 두께(px)
 * @param interval 대시와 간격의 길이(px)
 * @param phase 대시 패턴 시작 오프셋(px)
 */
fun Modifier.drawDashBorder(
    color: Color = Color.Gray,
    shape: Shape = RectangleShape,
    width: Float = 1f,
    interval: Float = 10f,
    phase: Float = 0f,
) = this.drawWithCache {
    val outline = shape.createOutline(size, layoutDirection, this)
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(interval, interval), phase)
    val stroke = Stroke(width = width, pathEffect = pathEffect)

    onDrawBehind {
        when (outline) {
            is Outline.Rectangle -> {
                drawRect(
                    color = color,
                    topLeft = outline.rect.topLeft,
                    size = outline.rect.size,
                    style = stroke,
                )
            }

            is Outline.Rounded -> {
                drawPath(
                    path = Path().apply { addRoundRect(outline.roundRect) },
                    color = color,
                    style = stroke,
                )
            }

            is Outline.Generic -> {
                drawPath(
                    path = outline.path,
                    color = color,
                    style = stroke,
                )
            }
        }
    }
}

@BasicPreview
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
