package com.team.prezel.core.designsystem.component.voice

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlin.math.ceil

private const val MIN_REACTIVE_VOLUME = 0.12f
private const val MIN_WAVE_VOLUME = 0.1f
private const val RAW_SAMPLES_PER_WAVE_BAR = 2
private const val WAVE_SCROLL_DURATION_MILLIS = 100

/**
 * 새 음량 샘플이 오른쪽에서 들어오고 기존 기록이 왼쪽으로 흐르는 Voice 파형이다.
 *
 * [volumes]는 무음 구간을 포함해 50ms 주기로 쌓인 값이어야 한다. 녹음과 재생의 streaming 파형은
 * 원본 샘플 두 개의 최고값을 100ms짜리 막대 하나로 합쳐 떨림을 줄이고, 무음도 최소 높이로 이동시킨다.
 * [usesStreamingLayout]은 녹음·녹음 일시정지·재생 상태에서 파형 기록을 오른쪽 기준으로 배치할 때 사용한다.
 * 실제 이동은 [status]가 [VoiceChromeStatus.LISTENING]일 때만 진행해 일시정지 중에는 현재 파형을 유지한다.
 * 수평 위치는 현재 막대 개수를 목표로 하는 절대 scroll position으로 계산한다. 새 막대가 추가된
 * 프레임에서도 기존 막대의 좌표가 유지되므로, list 갱신과 이후 offset 보정이 엇갈려 발생하던 좌우 떨림을 방지한다.
 * 각 갱신은 남은 거리와 무관하게 100ms 안에 최신 목표에 도달해, 샘플 간격의 미세한 오차가 누적되어 파형이 화면 밖으로 밀리지 않게 한다.
 */
@Composable
fun PrezelVoiceChromeWave(
    modifier: Modifier = Modifier,
    status: VoiceChromeStatus = VoiceChromeStatus.IDLE,
    volumes: ImmutableList<Float> = persistentListOf(),
    showBaseline: Boolean = true,
    usesStreamingLayout: Boolean = false,
) {
    val adjustedVolumes = remember(status, volumes, usesStreamingLayout) {
        volumes.adjustForVoiceChrome(
            status = status,
            usesStreamingLayout = usesStreamingLayout,
        )
    }
    val scrollPosition = remember(usesStreamingLayout) {
        Animatable(adjustedVolumes.size.toFloat())
    }

    LaunchedEffect(status, adjustedVolumes.size, usesStreamingLayout) {
        val targetPosition = adjustedVolumes.size.toFloat()

        if (!usesStreamingLayout || adjustedVolumes.isEmpty()) {
            scrollPosition.snapTo(targetPosition)
            return@LaunchedEffect
        }
        if (targetPosition < scrollPosition.value) {
            scrollPosition.snapTo(targetPosition)
            return@LaunchedEffect
        }
        if (status != VoiceChromeStatus.LISTENING || targetPosition == scrollPosition.value) {
            return@LaunchedEffect
        }

        scrollPosition.animateTo(
            targetValue = targetPosition,
            animationSpec = tween(
                durationMillis = WAVE_SCROLL_DURATION_MILLIS,
                easing = LinearEasing,
            ),
        )
    }

    Spacer(
        modifier = modifier.drawVoiceChromeWave(
            status = status,
            volumes = adjustedVolumes,
            showBaseline = showBaseline,
            usesStreamingLayout = usesStreamingLayout,
            scrollPositionProvider = { scrollPosition.value },
        ),
    )
}

/**
 * 무음은 최소 높이로 변환하고 음성 입력값은 막대 높이 계산 범위 안으로 보정한다.
 * 라이브 녹음에서는 50ms 샘플 두 개의 최고값을 사용해 응답성은 유지하면서 갱신 빈도를 낮춘다.
 */
private fun ImmutableList<Float>.adjustForVoiceChrome(
    status: VoiceChromeStatus,
    usesStreamingLayout: Boolean,
): ImmutableList<Float> =
    when (status) {
        VoiceChromeStatus.IDLE -> persistentListOf()

        VoiceChromeStatus.LISTENING,
        VoiceChromeStatus.WAITING,
        -> {
            val clippedVolumes = this
                .map { volume ->
                    if (volume <= MIN_REACTIVE_VOLUME) {
                        MIN_WAVE_VOLUME
                    } else {
                        volume.coerceIn(
                            minimumValue = MIN_WAVE_VOLUME,
                            maximumValue = 1f,
                        )
                    }
                }

            if (!usesStreamingLayout) {
                clippedVolumes.toImmutableList()
            } else {
                List(clippedVolumes.size / RAW_SAMPLES_PER_WAVE_BAR) { barIndex ->
                    val firstSampleIndex = barIndex * RAW_SAMPLES_PER_WAVE_BAR
                    maxOf(
                        clippedVolumes[firstSampleIndex],
                        clippedVolumes[firstSampleIndex + 1],
                    )
                }.toImmutableList()
            }
        }
    }

@Composable
private fun Modifier.drawVoiceChromeWave(
    status: VoiceChromeStatus,
    volumes: ImmutableList<Float>,
    showBaseline: Boolean,
    usesStreamingLayout: Boolean,
    scrollPositionProvider: () -> Float,
): Modifier {
    val colors = PrezelTheme.colors

    return fillMaxWidth()
        .height(60.dp)
        .drawWithCache {
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
                    usesStreamingLayout = usesStreamingLayout,
                    scrollPosition = scrollPositionProvider(),
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
    usesStreamingLayout: Boolean,
    scrollPosition: Float,
) {
    drawVoiceChromeWaveBars(
        status = status,
        volumes = volumes,
        config = config,
        usesStreamingLayout = usesStreamingLayout,
        scrollPosition = scrollPosition,
    )

    drawVoiceChromeWaveBaseline(
        visible = showBaseline,
        color = baselineColor,
        strokeWidth = config.baselineStrokeWidth,
    )
}

/**
 * 아직 샘플이 채워지지 않은 영역에만 고정 기준선을 그리고 실제 샘플은 별도로 이동시킨다.
 * 기준선을 전체 폭에 그리면 이동 중인 파란 막대 사이로 회색 막대가 노출되어 서로 미는 것처럼 보인다.
 * 최신 샘플을 한 칸 오른쪽 바깥에서 시작하면 기존 파형의 위치가 끊기지 않고 이어진다.
 */
private fun DrawScope.drawVoiceChromeWaveBars(
    status: VoiceChromeStatus,
    volumes: ImmutableList<Float>,
    config: VoiceChromeWaveDrawConfig,
    usesStreamingLayout: Boolean,
    scrollPosition: Float,
) {
    val barCount = ceil(size.width / config.barSpacing).toInt()
    val visibleSampleCount = volumes.size.coerceAtMost(
        if (usesStreamingLayout) barCount + 1 else barCount,
    )
    val filledBarCount = if (status == VoiceChromeStatus.IDLE) {
        0
    } else {
        visibleSampleCount.coerceAtMost(barCount)
    }

    repeat(barCount - filledBarCount) { emptyIndex ->
        val emptyBarIndex = filledBarCount + emptyIndex
        val barX = if (usesStreamingLayout) {
            size.width - config.barWidth - (emptyBarIndex * config.barSpacing)
        } else {
            emptyBarIndex * config.barSpacing
        }
        drawVoiceChromeWaveBar(
            status = VoiceChromeStatus.IDLE,
            volume = MIN_WAVE_VOLUME,
            barX = barX,
            config = config,
        )
    }

    if (
        usesStreamingLayout &&
        status != VoiceChromeStatus.IDLE &&
        scrollPosition < volumes.size.toFloat()
    ) {
        drawVoiceChromeWaveBar(
            status = VoiceChromeStatus.IDLE,
            volume = MIN_WAVE_VOLUME,
            barX = size.width - config.barWidth,
            config = config,
        )
    }

    if (status == VoiceChromeStatus.IDLE) return

    val firstVisibleIndex = volumes.size - visibleSampleCount

    repeat(visibleSampleCount) { visibleIndex ->
        val volume = if (!usesStreamingLayout && volumes.size > barCount) {
            volumes.maxVolumeInBucket(
                bucketIndex = visibleIndex,
                bucketCount = barCount,
            )
        } else {
            volumes[firstVisibleIndex + visibleIndex]
        }
        val sampleIndex = firstVisibleIndex + visibleIndex
        val barX = if (usesStreamingLayout) {
            val distanceFromRight = scrollPosition - 1f - sampleIndex
            size.width - config.barWidth -
                (distanceFromRight * config.barSpacing)
        } else {
            visibleIndex * config.barSpacing
        }

        if (barX + config.barWidth <= 0f || barX >= size.width) return@repeat

        drawVoiceChromeWaveBar(
            status = status,
            volume = volume,
            barX = barX,
            config = config,
        )
    }
}

/**
 * 완성된 녹음의 전체 시간축을 [bucketCount]개의 막대로 축약한다.
 * 각 구간의 최고값을 사용해 짧은 음성 peak가 overview에서 사라지지 않게 한다.
 */
private fun ImmutableList<Float>.maxVolumeInBucket(
    bucketIndex: Int,
    bucketCount: Int,
): Float {
    val startIndex = bucketIndex * size / bucketCount
    val endIndex = ((bucketIndex + 1) * size / bucketCount)
        .coerceAtLeast(startIndex + 1)

    var maxVolume = MIN_WAVE_VOLUME
    for (sampleIndex in startIndex until endIndex) {
        maxVolume = maxOf(maxVolume, this[sampleIndex])
    }

    return maxVolume
}

private fun DrawScope.drawVoiceChromeWaveBar(
    status: VoiceChromeStatus,
    volume: Float,
    barX: Float,
    config: VoiceChromeWaveDrawConfig,
) {
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
