package com.team.prezel.core.designsystem.component.voice

import androidx.annotation.FloatRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.LargeDevicePreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSurface
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

private const val MIN_REACTIVE_VOLUME = 0.12f
private const val MIN_WAVE_VOLUME = 0.1f

@Composable
fun PrezelVoiceChromeWave(
    modifier: Modifier = Modifier,
    status: VoiceChromeStatus = VoiceChromeStatus.IDLE,
    volumes: ImmutableList<Float> = persistentListOf(),
    showBaseline: Boolean = true,
) {
    val adjustedVolumes = when (status) {
        VoiceChromeStatus.IDLE -> persistentListOf()

        VoiceChromeStatus.LISTENING,
        VoiceChromeStatus.WAITING,
        -> {
            val clippedVolumes = volumes
                .filter { volume -> volume > MIN_REACTIVE_VOLUME }
                .map { volume ->
                    volume.coerceIn(
                        minimumValue = MIN_WAVE_VOLUME,
                        maximumValue = 1f,
                    )
                }

            clippedVolumes.toImmutableList()
        }
    }

    Spacer(
        modifier = modifier.drawVoiceChromeWave(
            status = status,
            volumes = adjustedVolumes,
            showBaseline = showBaseline,
        ),
    )
}

@Composable
private fun Modifier.drawVoiceChromeWave(
    status: VoiceChromeStatus,
    volumes: ImmutableList<Float>,
    showBaseline: Boolean,
): Modifier {
    val colors = PrezelTheme.colors

    return fillMaxWidth().height(60.dp).drawWithCache {
        val barWidth = 2.dp.toPx()
        val barSpacing = 6.dp.toPx()
        val minBarHeight = 4.dp.toPx()
        val maxBarHeight = 40.dp.toPx()
        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
        val activeBrush = Brush.horizontalGradient(
            colorStops = arrayOf(
                0f to colors.interactiveSmall,
                0.5f to colors.interactiveRegular,
                1f to colors.interactiveSmall,
            ),
            startX = 0f,
            endX = size.width,
        )
        val drawConfig = VoiceChromeWaveDrawConfig(
            barWidth = barWidth,
            barSpacing = barSpacing,
            minBarHeight = minBarHeight,
            maxBarHeight = maxBarHeight,
            barRadius = barRadius,
            activeBrush = activeBrush,
            waitingColor = colors.interactiveXSmall,
            idleColor = colors.bgDisabled,
            baselineStrokeWidth = 1.dp.toPx(),
        )

        onDrawBehind {
            drawVoiceChromeWaveContent(
                status = status,
                volumes = volumes,
                config = drawConfig,
                showBaseline = showBaseline,
                baselineColor = colors.borderRegular,
            )
        }
    }
}

private data class VoiceChromeWaveDrawConfig(
    val barWidth: Float,
    val barSpacing: Float,
    val minBarHeight: Float,
    val maxBarHeight: Float,
    val barRadius: CornerRadius,
    val activeBrush: Brush,
    val waitingColor: Color,
    val idleColor: Color,
    val baselineStrokeWidth: Float,
)

private fun DrawScope.drawVoiceChromeWaveContent(
    status: VoiceChromeStatus,
    volumes: ImmutableList<Float>,
    config: VoiceChromeWaveDrawConfig,
    showBaseline: Boolean,
    baselineColor: Color,
) {
    drawVoiceChromeWaveBars(
        status = status,
        volumes = volumes,
        config = config,
    )

    drawVoiceChromeWaveBaseline(
        visible = showBaseline,
        color = baselineColor,
        strokeWidth = config.baselineStrokeWidth,
    )
}

private fun DrawScope.drawVoiceChromeWaveBars(
    status: VoiceChromeStatus,
    volumes: ImmutableList<Float>,
    config: VoiceChromeWaveDrawConfig,
) {
    var barX = -config.barWidth
    var barIndex = 0
    val barCount = (size.width / config.barSpacing).roundToInt() + 1

    while (barX < size.width + config.barSpacing) {
        val volume = volumes.sampleVolume(
            index = barIndex,
            sampleCount = barCount,
        )
        val barHeight = config.volumeToBarHeight(volume)
        val barTop = (size.height - barHeight) / 2f
        val topLeft = Offset(x = barX, y = barTop)
        val barSize = Size(width = config.barWidth, height = barHeight)

        when (status) {
            VoiceChromeStatus.IDLE -> drawRoundRect(
                color = config.idleColor,
                topLeft = topLeft,
                size = barSize,
                cornerRadius = config.barRadius,
            )

            VoiceChromeStatus.LISTENING -> drawRoundRect(
                brush = config.activeBrush,
                topLeft = topLeft,
                size = barSize,
                cornerRadius = config.barRadius,
            )

            VoiceChromeStatus.WAITING -> drawRoundRect(
                color = config.waitingColor,
                topLeft = topLeft,
                size = barSize,
                cornerRadius = config.barRadius,
            )
        }

        barX += config.barSpacing
        barIndex += 1
    }
}

private fun DrawScope.drawVoiceChromeWaveBaseline(
    visible: Boolean,
    color: Color,
    strokeWidth: Float,
) {
    if (!visible) return

    drawLine(
        color = color,
        start = Offset(x = size.width / 2f, y = 0f),
        end = Offset(x = size.width / 2f, y = size.height),
        strokeWidth = strokeWidth,
    )
}

private fun VoiceChromeWaveDrawConfig.volumeToBarHeight(volume: Float): Float {
    val volumeProgress = (volume - MIN_WAVE_VOLUME) / (1f - MIN_WAVE_VOLUME)

    return minBarHeight + volumeProgress * (maxBarHeight - minBarHeight)
}

private fun ImmutableList<Float>.sampleVolume(
    index: Int,
    sampleCount: Int,
): Float {
    if (isEmpty() || sampleCount <= 0) return MIN_WAVE_VOLUME

    val firstVisibleIndex = (size - sampleCount).coerceAtLeast(0)
    val sampleIndex = firstVisibleIndex + index

    return getOrElse(sampleIndex) { MIN_WAVE_VOLUME }
}

@LargeDevicePreview
@Composable
private fun PrezelVoiceChromeWaveComponentPreview() {
    PreviewSurface {
        PreviewColumn {
            VoiceChromeWavePreviewSection(
                title = "Show Baseline - True",
                showBaseline = true,
            )
            VoiceChromeWavePreviewSection(
                title = "Show Baseline - False",
                showBaseline = false,
            )
            VoiceChromeWaveVolumePreviewSection()
        }
    }
}

@Composable
private fun VoiceChromeWavePreviewSection(
    title: String,
    showBaseline: Boolean,
) {
    Column {
        Text(
            text = title,
            style = PrezelTheme.typography.body2Bold,
            color = PrezelTheme.colors.textLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        VoiceChromeWaveStatusPreviewRow(showBaseline = showBaseline)
    }
}

@Composable
private fun VoiceChromeWaveStatusPreviewRow(showBaseline: Boolean) {
    Row {
        VoiceChromeWavePreviewItem(label = "Status - Idle") {
            PrezelVoiceChromeWave(
                status = VoiceChromeStatus.IDLE,
                showBaseline = showBaseline,
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        VoiceChromeWavePreviewItem(label = "Status - Listening") {
            PrezelVoiceChromeWave(
                status = VoiceChromeStatus.LISTENING,
                volumes = previewVolumes(
                    peakVolume = 0.75f,
                ),
                showBaseline = showBaseline,
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        VoiceChromeWavePreviewItem(label = "Status - Waiting") {
            PrezelVoiceChromeWave(
                status = VoiceChromeStatus.WAITING,
                showBaseline = showBaseline,
            )
        }
    }
}

@Composable
private fun VoiceChromeWaveVolumePreviewSection() {
    Column {
        Text(
            text = "Volume",
            style = PrezelTheme.typography.body2Bold,
            color = PrezelTheme.colors.textLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            VoiceChromeWaveVolumePreviewItem(
                label = "Volume - Min",
                peakVolume = 0.1f,
            )
            Spacer(modifier = Modifier.width(20.dp))
            VoiceChromeWaveVolumePreviewItem(
                label = "Volume - Max",
                peakVolume = 1f,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            VoiceChromeWaveVolumePreviewItem(
                label = "Volume - 25%",
                peakVolume = 0.25f,
            )
            Spacer(modifier = Modifier.width(20.dp))
            VoiceChromeWaveVolumePreviewItem(
                label = "Volume - 50%",
                peakVolume = 0.5f,
            )
            Spacer(modifier = Modifier.width(20.dp))
            VoiceChromeWaveVolumePreviewItem(
                label = "Volume - 75%",
                peakVolume = 0.75f,
            )
        }
    }
}

@Composable
private fun VoiceChromeWaveVolumePreviewItem(
    label: String,
    @FloatRange(from = 0.0, to = 1.0) peakVolume: Float,
) {
    VoiceChromeWavePreviewItem(label = label) {
        PrezelVoiceChromeWave(
            status = VoiceChromeStatus.LISTENING,
            volumes = previewVolumes(
                peakVolume = peakVolume,
            ),
        )
    }
}

@Composable
private fun VoiceChromeWavePreviewItem(
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
private fun PrezelVoiceChromeWaveIdleToListeningPreview() {
    var status by remember { mutableStateOf(VoiceChromeStatus.IDLE) }

    PreviewSurface {
        PreviewColumn {
            VoiceChromeWavePreviewItem(label = "Idle > Listening") {
                PrezelVoiceChromeWave(
                    modifier = Modifier.clickable {
                        status = VoiceChromeStatus.LISTENING
                    },
                    status = status,
                    volumes = previewVolumes(
                        peakVolume = 1f,
                    ),
                    showBaseline = false,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelVoiceChromeWaveListeningToWaitingPreview() {
    var status by remember { mutableStateOf(VoiceChromeStatus.LISTENING) }

    PreviewSurface {
        PreviewColumn {
            VoiceChromeWavePreviewItem(label = "Listening > Waiting") {
                PrezelVoiceChromeWave(
                    modifier = Modifier.clickable {
                        status = VoiceChromeStatus.WAITING
                    },
                    status = status,
                    volumes = previewVolumes(
                        peakVolume = 1f,
                    ),
                    showBaseline = false,
                )
            }
        }
    }
}

private fun previewVolumes(
    @FloatRange(from = 0.0, to = 1.0) peakVolume: Float,
): ImmutableList<Float> =
    List(61) { index ->
        val volume = PreviewVolumePattern[index % PreviewVolumePattern.size]
        0.1f + (volume - 0.1f) * ((peakVolume - 0.1f) / 0.9f)
    }.toImmutableList()

private val PreviewVolumePattern = listOf(
    0.24f,
    0.42f,
    0.30f,
    0.56f,
    0.38f,
    0.70f,
    0.48f,
    0.86f,
    1.00f,
    0.78f,
    0.62f,
    0.44f,
    0.58f,
    0.36f,
    0.28f,
)
