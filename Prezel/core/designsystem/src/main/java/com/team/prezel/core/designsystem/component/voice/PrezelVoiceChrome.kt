package com.team.prezel.core.designsystem.component.voice

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.LargeDevicePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
enum class VoiceChromeStatus {
    IDLE,
    LISTENING,
    WAITING,
}

@Immutable
enum class VoiceChromeGradient {
    NONE,
    MIN,
    MAX,
}

@Composable
fun PrezelVoiceChrome(
    titleText: String,
    modifier: Modifier = Modifier,
    status: VoiceChromeStatus = VoiceChromeStatus.IDLE,
    gradient: VoiceChromeGradient = VoiceChromeGradient.NONE,
) {
    PrezelVoiceChromeContent(
        titleText = titleText,
        modifier = modifier,
        status = status,
        gradientStop = gradient.stop,
    )
}

@Composable
private fun PrezelVoiceChromeContent(
    titleText: String,
    modifier: Modifier = Modifier,
    status: VoiceChromeStatus = VoiceChromeStatus.IDLE,
    gradientStop: Float = VoiceChromeGradient.NONE.stop,
) {
    val lineColor = when (status) {
        VoiceChromeStatus.IDLE,
        VoiceChromeStatus.LISTENING,
        -> PrezelTheme.colors.interactiveRegular

        VoiceChromeStatus.WAITING -> PrezelTheme.colors.borderLarge
    }

    Box(
        modifier = modifier
            .size(width = 360.dp, height = 160.dp)
            .voiceChromeBackground(
                status = status,
                gradientStop = gradientStop,
                color = PrezelTheme.colors.interactiveRegular,
            ),
        contentAlignment = Alignment.Center,
    ) {
        VoiceChromeTitle(
            titleText = titleText,
            status = status,
        )

        VoiceChromeLine(
            status = status,
            color = lineColor,
        )
    }
}

@Composable
private fun VoiceChromeTitle(
    titleText: String,
    status: VoiceChromeStatus,
) {
    val baseStyle = PrezelTheme.typography.title1Bold.copy(textAlign = TextAlign.Center)

    when (status) {
        VoiceChromeStatus.IDLE -> Text(
            text = titleText,
            style = baseStyle.withIdleTitleBrush(),
        )

        VoiceChromeStatus.LISTENING -> Text(
            text = stringResource(R.string.core_designsystem_voice_chrome_listening),
            style = baseStyle,
            color = PrezelTheme.colors.interactiveRegular,
        )

        VoiceChromeStatus.WAITING -> Text(
            text = stringResource(R.string.core_designsystem_voice_chrome_waiting),
            style = baseStyle,
            color = PrezelTheme.colors.textMedium,
        )
    }
}

@Composable
private fun TextStyle.withIdleTitleBrush(): TextStyle {
    val colors = PrezelTheme.colors

    return copy(
        brush = Brush.horizontalGradient(
            colorStops = arrayOf(
                0f to colors.interactiveSmall,
                0.5f to colors.interactiveRegular,
                1f to colors.interactiveSmall,
            ),
        ),
    )
}

@Composable
private fun BoxScope.VoiceChromeLine(
    status: VoiceChromeStatus,
    color: Color,
) {
    val visible = status != VoiceChromeStatus.IDLE

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .size(width = 360.dp, height = 4.dp)
            .drawWithCache {
                val lineBrush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0f to color.copy(alpha = 0f),
                        0.5f to color,
                        1f to color.copy(alpha = 0f),
                    ),
                )

                onDrawBehind {
                    if (visible) {
                        drawRect(brush = lineBrush)
                    }
                }
            },
    )
}

private fun Modifier.voiceChromeBackground(
    status: VoiceChromeStatus,
    gradientStop: Float,
    color: Color,
): Modifier =
    drawWithCache {
        val shouldDrawGradient = status == VoiceChromeStatus.LISTENING && gradientStop > 0f
        val gradientBrush = if (shouldDrawGradient) {
            Brush.radialGradient(
                colorStops = arrayOf(
                    0f to color,
                    gradientStop to color.copy(alpha = 0f),
                ),
                center = Offset(size.width / 2f, size.height),
                radius = size.width,
            )
        } else {
            null
        }

        onDrawBehind {
            if (gradientBrush != null) {
                clipRect {
                    withTransform(
                        {
                            scale(
                                scaleX = 1f,
                                scaleY = 0.45f,
                                pivot = Offset(size.width / 2f, size.height),
                            )
                        },
                    ) {
                        drawCircle(
                            brush = gradientBrush,
                            radius = size.width,
                            center = Offset(size.width / 2f, size.height),
                        )
                    }
                }
            }
        }
    }

private val VoiceChromeGradient.stop: Float
    get() = when (this) {
        VoiceChromeGradient.NONE -> 0f
        VoiceChromeGradient.MIN -> 0.24f
        VoiceChromeGradient.MAX -> 0.44f
    }

@LargeDevicePreview
@Composable
private fun PrezelVoiceChromeComponentPreview() {
    PrezelTheme {
        Column {
            Row {
                PrezelVoiceChrome(titleText = "지금부터 발표해볼까요?")

                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.WAITING,
                )
            }

            Row {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.NONE,
                )
                Spacer(modifier = Modifier.width(20.dp))
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.MIN,
                )
                Spacer(modifier = Modifier.width(20.dp))
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.MAX,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@BasicPreview
@Composable
private fun PrezelVoiceChromeAnimatedPreview() {
    val transition = rememberInfiniteTransition(label = "VoiceChromePreviewTransition")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1_200,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "VoiceChromePreviewGradient",
    )
    val gradientStop = VoiceChromeGradient.MIN.stop +
        (VoiceChromeGradient.MAX.stop - VoiceChromeGradient.MIN.stop) * progress

    PrezelTheme {
        PrezelVoiceChromeContent(
            titleText = "지금부터 발표해볼까요?",
            status = VoiceChromeStatus.LISTENING,
            gradientStop = gradientStop,
        )
    }
}
