package com.team.prezel.core.designsystem.util

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dropShadowCache(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    shape: Shape,
    blurStyle: BlurMaskFilter.Blur = BlurMaskFilter.Blur.NORMAL,
): Modifier =
    drawWithCache {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), blurStyle))
        }
        frameworkPaint.color = color.toArgb()

        val leftPixel = offsetX.toPx()
        val topPixel = offsetY.toPx()

        val path = Path().apply {
            addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache))
        }

        onDrawBehind {
            drawIntoCanvas { canvas ->
                canvas.translate(leftPixel, topPixel)
                canvas.drawPath(path, paint)
                canvas.translate(-leftPixel, -topPixel)
            }
        }
    }
