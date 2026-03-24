package com.team.prezel.core.designsystem.component.base

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
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

private fun Modifier.dropShadow(
    shadows: List<PrezelDropShadowDefaults.PrezelShadowToken>,
    borderRadius: Dp = 0.dp,
    isBackgroundTransparent: Boolean = false,
) = this.then(
    Modifier.drawBehind {
        // Early return으로 불필요한 연산 방지
        if (shadows.isEmpty()) return@drawBehind

        // BlurMaskFilter 캐시 - dropShadow 함수 내부에서만 사용
        val blurMaskFilterCache = mutableMapOf<Float, BlurMaskFilter>()

        this.drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()

            // transparent 배경인 경우를 위한 Path - 재사용 가능하도록 미리 생성
            var clipPath: Path? = null
            val borderRadiusPx = borderRadius.toPx()

            if (isBackgroundTransparent && borderRadiusPx > 0f) {
                clipPath = Path().apply {
                    addRoundRect(
                        androidx.compose.ui.geometry.RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            radiusX = borderRadiusPx,
                            radiusY = borderRadiusPx,
                        ),
                    )
                }
            }

            // shadow를 뒤에서부터 그려서 올바른 layering 구현
            for (i in shadows.size - 1 downTo 0) {
                val shadow = shadows[i]

                // 색상 설정
                frameworkPaint.color = shadow.color.toArgb()

                // BlurMaskFilter 캐싱 및 재사용 - 같은 drawBehind 호출 내에서만 캐싱
                val blurRadiusPx = shadow.blurRadius.toPx()
                if (blurRadiusPx > 0f) {
                    frameworkPaint.maskFilter = blurMaskFilterCache.getOrPut(blurRadiusPx) {
                        BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
                    }
                } else {
                    frameworkPaint.maskFilter = null
                }

                // 그리기 영역 계산 - 변수 재사용으로 메모리 할당 최소화
                val spreadPixel = shadow.spreadRadius.toPx()
                val offsetXPx = shadow.offsetX.toPx()
                val offsetYPx = shadow.offsetY.toPx()

                val left = -spreadPixel + offsetXPx
                val top = -spreadPixel + offsetYPx
                val right = size.width + spreadPixel + offsetXPx
                val bottom = size.height + spreadPixel + offsetYPx

                // Clipping 처리 최적화
                var needsRestore = false
                if (isBackgroundTransparent) {
                    canvas.save()
                    needsRestore = true

                    // 미리 생성된 clipPath 재사용
                    clipPath?.let { path ->
                        canvas.clipPath(path, ClipOp.Difference)
                    }
                }

                // 그리기 - 조건부 radius 최적화
                if (borderRadiusPx > 0f) {
                    canvas.drawRoundRect(
                        left = left,
                        top = top,
                        right = right,
                        bottom = bottom,
                        radiusX = borderRadiusPx,
                        radiusY = borderRadiusPx,
                        paint = paint,
                    )
                } else {
                    // radius가 0이면 더 빠른 drawRect 사용
                    canvas.drawRect(
                        left = left,
                        top = top,
                        right = right,
                        bottom = bottom,
                        paint = paint,
                    )
                }

                if (needsRestore) {
                    canvas.restore()
                }
            }
        }
    },
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

    data class Default(
        override val borderRadius: Dp,
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
private fun WantedDropShadowPreview() {
    PrezelTheme {
        Surface {
            Column(
                modifier = Modifier
                    .background(PrezelTheme.colors.bgRegular)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(color = Color.Green, shape = RoundedCornerShape(10.dp))
                        .prezelDropShadow(style = PrezelDropShadowDefaults.None),
                )
                Box(
                    Modifier
                        .size(100.dp)
                        .prezelDropShadow(
                            style = PrezelDropShadowDefaults.Default(
                                borderRadius = 10.dp,
                                backgroundColor = Color.Green,
                            ),
                        ),
                )
            }
        }
    }
}
