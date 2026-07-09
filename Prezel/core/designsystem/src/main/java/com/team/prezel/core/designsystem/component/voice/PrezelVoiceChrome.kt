package com.team.prezel.core.designsystem.component.voice

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.LargeDevicePreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSurface
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
    val shouldAnimateGradient = status == VoiceChromeStatus.LISTENING &&
        gradient != VoiceChromeGradient.NONE
    val gradientStop = if (shouldAnimateGradient) {
        val transition = rememberInfiniteTransition(label = "VoiceChromeGradientTransition")
        val animatedGradientStop by transition.animateFloat(
            initialValue = gradient.initialAnimatedStop,
            targetValue = gradient.targetAnimatedStop,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2400,
                    delayMillis = 160,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "VoiceChromeGradientStop",
        )
        animatedGradientStop
    } else {
        VoiceChromeGradient.NONE.stop
    }

    PrezelVoiceChromeContent(
        titleText = titleText,
        modifier = modifier,
        status = status,
        gradientStop = gradientStop,
    )
}

@Composable
private fun PrezelVoiceChromeContent(
    titleText: String,
    modifier: Modifier = Modifier,
    status: VoiceChromeStatus = VoiceChromeStatus.IDLE,
    gradientStop: Float = VoiceChromeGradient.NONE.stop,
) {
    val targetLineColor = when (status) {
        VoiceChromeStatus.IDLE,
        VoiceChromeStatus.LISTENING,
        -> PrezelTheme.colors.interactiveRegular

        VoiceChromeStatus.WAITING -> PrezelTheme.colors.borderLarge
    }
    val lineColor by animateColorAsState(
        targetValue = targetLineColor,
        animationSpec = tween(durationMillis = 280),
        label = "VoiceChromeLineColor",
    )
    val targetTitleColor = when (status) {
        VoiceChromeStatus.IDLE,
        VoiceChromeStatus.LISTENING,
        -> PrezelTheme.colors.interactiveRegular

        VoiceChromeStatus.WAITING -> PrezelTheme.colors.textMedium
    }
    val titleColor by animateColorAsState(
        targetValue = targetTitleColor,
        animationSpec = tween(durationMillis = 280),
        label = "VoiceChromeTitleColor",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
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
            color = titleColor,
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
    color: Color,
) {
    val baseStyle = PrezelTheme.typography.title1Bold.copy(textAlign = TextAlign.Center)

    when (status) {
        VoiceChromeStatus.IDLE -> Text(
            text = titleText,
            style = baseStyle.withIdleTitleBrush(),
        )

        VoiceChromeStatus.LISTENING -> Text(
            text = titleText,
            style = baseStyle,
            color = color,
        )

        VoiceChromeStatus.WAITING -> Text(
            text = titleText,
            style = baseStyle,
            color = color,
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
    val lineProgress by animateFloatAsState(
        targetValue = if (status == VoiceChromeStatus.IDLE) 0f else 1f,
        animationSpec = tween(
            durationMillis = 560,
            easing = CubicBezierEasing(0f, 0f, 0.58f, 1f),
        ),
        label = "VoiceChromeLineProgress",
    )

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(4.dp)
            .drawWithCache {
                val lineBrush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0f to color.copy(alpha = 0f),
                        0.5f to color,
                        1f to color.copy(alpha = 0f),
                    ),
                )

                onDrawBehind {
                    val halfLineWidth = size.width * lineProgress / 2f
                    clipRect(
                        left = size.width / 2f - halfLineWidth,
                        right = size.width / 2f + halfLineWidth,
                    ) {
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
                0f to color.copy(alpha = 1f),
                gradientStop * 0.12f to color.copy(alpha = 0.86f),
                gradientStop * 0.24f to color.copy(alpha = 0.68f),
                gradientStop * 0.38f to color.copy(alpha = 0.48f),
                gradientStop * 0.54f to color.copy(alpha = 0.30f),
                gradientStop * 0.72f to color.copy(alpha = 0.14f),
                gradientStop * 0.88f to color.copy(alpha = 0.05f),
                gradientStop to color.copy(alpha = 0f),
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
        VoiceChromeGradient.MIN -> 0.28f
        VoiceChromeGradient.MAX -> 0.44f
    }

private val VoiceChromeGradient.initialAnimatedStop: Float
    get() = when (this) {
        VoiceChromeGradient.NONE,
        VoiceChromeGradient.MIN,
        -> VoiceChromeGradient.MIN.stop

        VoiceChromeGradient.MAX -> VoiceChromeGradient.MAX.stop
    }

private val VoiceChromeGradient.targetAnimatedStop: Float
    get() = when (this) {
        VoiceChromeGradient.NONE,
        VoiceChromeGradient.MIN,
        -> VoiceChromeGradient.MAX.stop

        VoiceChromeGradient.MAX -> VoiceChromeGradient.MIN.stop
    }

@LargeDevicePreview
@Composable
private fun PrezelVoiceChromeComponentPreview() {
    PreviewSurface {
        PreviewColumn {
            VoiceChromeStatusPreviewSection()
            Spacer(modifier = Modifier.height(4.dp))
            VoiceChromeGradientPreviewSection()
        }
    }
}

@Composable
private fun VoiceChromeStatusPreviewSection() {
    VoiceChromePreviewSection(title = "Status") {
        Row {
            VoiceChromePreviewItem(label = "Status - Idle") {
                PrezelVoiceChrome(titleText = "지금부터 발표해볼까요?")
            }
            VoiceChromePreviewItem(label = "Status - Listening") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.MIN,
                )
            }
            VoiceChromePreviewItem(label = "Status - Waiting") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.WAITING,
                )
            }
        }
    }
}

@Composable
private fun VoiceChromeGradientPreviewSection() {
    VoiceChromePreviewSection(title = "Gradient") {
        Row {
            VoiceChromePreviewItem(label = "Gradient - None") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.NONE,
                )
            }
            VoiceChromePreviewItem(label = "Gradient - Min") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.MIN,
                )
            }
            VoiceChromePreviewItem(label = "Gradient - Max") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.MAX,
                )
            }
        }
    }
}

@Composable
private fun VoiceChromePreviewSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = PrezelTheme.typography.body2Bold,
            color = PrezelTheme.colors.textLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun VoiceChromePreviewItem(
    label: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.width(360.dp)) {
        Text(
            text = label,
            style = PrezelTheme.typography.caption1Medium,
            color = PrezelTheme.colors.textMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@BasicPreview
@Composable
private fun PrezelVoiceChromeIdleMinTogglePreview() {
    var status by remember { mutableStateOf(VoiceChromeStatus.IDLE) }

    PreviewSurface {
        PreviewColumn {
            VoiceChromePreviewItem(label = "Idle <-> Min") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    modifier = Modifier.clickable {
                        status = if (status == VoiceChromeStatus.IDLE) {
                            VoiceChromeStatus.LISTENING
                        } else {
                            VoiceChromeStatus.IDLE
                        }
                    },
                    status = status,
                    gradient = VoiceChromeGradient.MIN,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelVoiceChromeMaxWaitingTogglePreview() {
    var status by remember { mutableStateOf(VoiceChromeStatus.LISTENING) }

    PreviewSurface {
        PreviewColumn {
            VoiceChromePreviewItem(label = "Max <-> Waiting") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    modifier = Modifier.clickable {
                        status = if (status == VoiceChromeStatus.WAITING) {
                            VoiceChromeStatus.LISTENING
                        } else {
                            VoiceChromeStatus.WAITING
                        }
                    },
                    status = status,
                    gradient = VoiceChromeGradient.MAX,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelVoiceChromeMaxMinMaxPreview() {
    PreviewSurface {
        PreviewColumn {
            VoiceChromePreviewItem(label = "Max <-> Min") {
                PrezelVoiceChrome(
                    titleText = "지금부터 발표해볼까요?",
                    status = VoiceChromeStatus.LISTENING,
                    gradient = VoiceChromeGradient.MAX,
                )
            }
        }
    }
}
