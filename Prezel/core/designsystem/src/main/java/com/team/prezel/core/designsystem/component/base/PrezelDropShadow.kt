package com.team.prezel.core.designsystem.component.base

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun Modifier.prezelDropShadow(style: PrezelDropShadowDefaults.PrezelShadowStyle): Modifier {
    val cachedShadows = remember(style) { style.getShadow() }

    return this
        .dropShadow(
            shadows = cachedShadows,
            borderRadius = style.borderRadius,
            isBackgroundTransparent = style.backgroundColor == Color.Transparent,
        ).background(
            color = style.backgroundColor,
            shape = RoundedCornerShape(style.borderRadius),
        )
}

/**
 * 여러 개의 shadow 토큰을 기반으로 컴포넌트 외곽에 그림자를 그립니다.
 */
private fun Modifier.dropShadow(
    shadows: List<PrezelDropShadowDefaults.PrezelShadowToken>,
    borderRadius: Dp,
    isBackgroundTransparent: Boolean = false,
): Modifier =
    drawWithCache {
        if (shadows.isEmpty()) {
            return@drawWithCache onDrawBehind {}
        }

        val borderRadiusPx = borderRadius.toPx()
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        val clipPath = createShadowClipPath(
            isBackgroundTransparent = isBackgroundTransparent,
            borderRadiusPx = borderRadiusPx,
            width = size.width,
            height = size.height,
        )
        val resolvedShadows = toResolvedShadows(shadows = shadows, size = size)

        onDrawBehind {
            drawShadows(
                shadows = resolvedShadows,
                paint = paint,
                frameworkPaint = frameworkPaint,
                clipPath = clipPath,
                borderRadiusPx = borderRadiusPx,
                isBackgroundTransparent = isBackgroundTransparent,
            )
        }
    }

/**
 * 투명 배경에서 내부 영역을 제외하기 위한 clip path를 생성합니다.
 */
private fun createShadowClipPath(
    isBackgroundTransparent: Boolean,
    borderRadiusPx: Float,
    width: Float,
    height: Float,
): Path? {
    if (!isBackgroundTransparent || borderRadiusPx <= 0f) return null

    return Path().apply {
        addRoundRect(
            roundRect = RoundRect(
                left = 0f,
                top = 0f,
                right = width,
                bottom = height,
                radiusX = borderRadiusPx,
                radiusY = borderRadiusPx,
            ),
        )
    }
}

/**
 * shadow 토큰 목록을 실제 draw에 사용할 값으로 변환합니다.
 */
private fun Density.toResolvedShadows(
    shadows: List<PrezelDropShadowDefaults.PrezelShadowToken>,
    size: Size,
): List<ResolvedShadow> {
    val maskFiltersByBlurRadius = mapBlurMaskFilters(shadows)

    return shadows.map { shadow ->
        val spreadRadiusPx = shadow.spreadRadius.toPx()
        val offsetXPx = shadow.offsetX.toPx()
        val offsetYPx = shadow.offsetY.toPx()
        val blurRadiusPx = shadow.blurRadius.toPx()

        ResolvedShadow(
            color = shadow.color.toArgb(),
            left = -spreadRadiusPx + offsetXPx,
            top = -spreadRadiusPx + offsetYPx,
            right = size.width + spreadRadiusPx + offsetXPx,
            bottom = size.height + spreadRadiusPx + offsetYPx,
            maskFilter = maskFiltersByBlurRadius.getValue(blurRadiusPx),
        )
    }
}

/**
 * blur 반경별로 재사용 가능한 mask filter를 생성합니다.
 */
private fun Density.mapBlurMaskFilters(shadows: List<PrezelDropShadowDefaults.PrezelShadowToken>): Map<Float, BlurMaskFilter?> =
    shadows
        .map { it.blurRadius.toPx() }
        .distinct()
        .associateWith { blurRadiusPx ->
            if (blurRadiusPx <= 0f) {
                null
            } else {
                BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
            }
        }

/**
 * 준비된 shadow 목록을 뒤쪽 레이어부터 순서대로 그립니다.
 */
private fun DrawScope.drawShadows(
    shadows: List<ResolvedShadow>,
    paint: Paint,
    frameworkPaint: android.graphics.Paint,
    clipPath: Path?,
    borderRadiusPx: Float,
    isBackgroundTransparent: Boolean,
) {
    drawIntoCanvas { drawCanvas ->
        for (index in shadows.indices.reversed()) {
            val shadow = shadows[index]
            frameworkPaint.color = shadow.color
            frameworkPaint.maskFilter = shadow.maskFilter

            drawCanvas.drawShadowLayer(
                shadow = shadow,
                paint = paint,
                clipPath = clipPath,
                borderRadiusPx = borderRadiusPx,
                width = size.width,
                height = size.height,
                isBackgroundTransparent = isBackgroundTransparent,
            )
        }

        frameworkPaint.maskFilter = null
    }
}

/**
 * 단일 shadow 레이어를 그리기 전에 clip을 적용하고 실제 도형을 렌더링합니다.
 */
private fun Canvas.drawShadowLayer(
    shadow: ResolvedShadow,
    paint: Paint,
    clipPath: Path?,
    borderRadiusPx: Float,
    width: Float,
    height: Float,
    isBackgroundTransparent: Boolean,
) {
    applyTransparentClip(
        clipPath = clipPath,
        borderRadiusPx = borderRadiusPx,
        width = width,
        height = height,
        isBackgroundTransparent = isBackgroundTransparent,
    )

    if (borderRadiusPx > 0f) {
        drawRoundRect(
            left = shadow.left,
            top = shadow.top,
            right = shadow.right,
            bottom = shadow.bottom,
            radiusX = borderRadiusPx,
            radiusY = borderRadiusPx,
            paint = paint,
        )
    } else {
        drawRect(
            left = shadow.left,
            top = shadow.top,
            right = shadow.right,
            bottom = shadow.bottom,
            paint = paint,
        )
    }

    if (isBackgroundTransparent) {
        restore()
    }
}

/**
 * 투명 배경인 경우 내부 영역을 제외하도록 clip을 적용합니다.
 */
private fun Canvas.applyTransparentClip(
    clipPath: Path?,
    borderRadiusPx: Float,
    width: Float,
    height: Float,
    isBackgroundTransparent: Boolean,
) {
    if (!isBackgroundTransparent) return

    save()

    if (borderRadiusPx > 0f) {
        clipPath?.let { path ->
            clipPath(path, ClipOp.Difference)
        }
        return
    }

    clipRect(
        left = 0f,
        top = 0f,
        right = width,
        bottom = height,
        clipOp = ClipOp.Difference,
    )
}

/**
 * shadow 하나를 draw 가능한 좌표와 paint 정보로 보관합니다.
 */
private data class ResolvedShadow(
    val color: Int,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val maskFilter: BlurMaskFilter?,
)

object PrezelDropShadowDefaults {
    sealed class PrezelShadowStyle(
        open val borderRadius: Dp,
        open val backgroundColor: Color,
    ) {
        abstract fun getShadow(): List<PrezelShadowToken>
    }

    data object None : PrezelShadowStyle(
        borderRadius = 0.dp,
        backgroundColor = Color.Transparent,
    ) {
        override fun getShadow(): List<PrezelShadowToken> = emptyList()
    }

    data class Regular(
        override val borderRadius: Dp = 0.dp,
        override val backgroundColor: Color = Color.Transparent,
    ) : PrezelShadowStyle(borderRadius, backgroundColor) {
        private val shadowList by lazy {
            listOf(
                PrezelShadowToken(
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    blurRadius = 4.dp,
                    spreadRadius = 0.dp,
                    color = Color(0x1F000713),
                ),
            )
        }

        override fun getShadow(): List<PrezelShadowToken> = shadowList
    }

    /**
     * data class PrezelShadowToken
     *
     * Shadow의 개별 속성을 정의하는 데이터 클래스입니다.
     * Shadow의 위치, 크기, 색상 등을 설정할 수 있습니다.
     *
     * @param offsetX Dp: Shadow의 수평 오프셋입니다.
     * @param offsetY Dp: Shadow의 수직 오프셋입니다.
     * @param blurRadius Dp: Shadow의 블러 반경입니다.
     * @param spreadRadius Dp: Shadow의 확산 반경입니다.
     * @param color Color: Shadow의 색상입니다.
     */
    data class PrezelShadowToken(
        val offsetX: Dp,
        val offsetY: Dp,
        val blurRadius: Dp,
        val spreadRadius: Dp,
        val color: Color,
    )
}

@ThemePreview
@Composable
private fun PrezelDropShadowPreview() {
    val styles = listOf(
        PrezelDropShadowDefaults.None,
        PrezelDropShadowDefaults.Regular(),
    )

    PrezelTheme {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "PrezelDropShadow",
                style = PrezelTheme.typography.body1Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            styles.forEach { style ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .prezelDropShadow(style = style),
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = style.javaClass.simpleName)
                }
            }
        }
    }
}
