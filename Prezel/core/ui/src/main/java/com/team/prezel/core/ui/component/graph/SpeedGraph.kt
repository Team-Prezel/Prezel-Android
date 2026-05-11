package com.team.prezel.core.ui.component.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlin.math.max

private const val DEFAULT_LOWER_BOUND = 180
private const val DEFAULT_UPPER_BOUND = 280
private const val GOOD_LOWER_BOUND = 210
private const val GOOD_UPPER_BOUND = 260

private const val FULL_CIRCLE_DEGREES = 270f
private const val GAP_CENTER_ANGLE = 90f
private const val GRAPH_STROKE_RATIO = 0.12f
private val GRAPH_SIZE = 160.dp
private val GRAPH_DIAMETER = 152.dp
private val GRAPH_LABEL_HORIZONTAL_PADDING = 30.dp

data class SpeedGraphColors(
    val baseTrackColor: Color,
    val goodRangeColor: Color,
    val goodGaugeColor: Color,
    val badGaugeColor: Color,
) {
    companion object {
        @Composable
        fun getDefault(
            baseTrackColor: Color = PrezelTheme.colors.bgMedium,
            goodRangeColor: Color = PrezelTheme.colors.bgLarge,
            goodGaugeColor: Color = PrezelTheme.colors.interactiveRegular,
            badGaugeColor: Color = PrezelTheme.colors.feedbackWarningRegular,
        ): SpeedGraphColors =
            SpeedGraphColors(
                baseTrackColor = baseTrackColor,
                goodRangeColor = goodRangeColor,
                goodGaugeColor = goodGaugeColor,
                badGaugeColor = badGaugeColor,
            )
    }
}

/**
 * 사용자의 속도를 시각화하는 컴포넌트입니다.
 *
 * @param userGauge 유저의 속도를 보여주는 영역
 * @param goodRange 적절한 속도의 범주를 보여주는 영역
 * @param baseRange 그래프 기준 영역
 */
@Composable
fun SpeedGraph(
    userGauge: Int,
    modifier: Modifier = Modifier,
    goodRange: IntRange = IntRange(GOOD_LOWER_BOUND, GOOD_UPPER_BOUND),
    baseRange: IntRange = IntRange(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND),
    colors: SpeedGraphColors = SpeedGraphColors.getDefault(),
) {
    Box(
        modifier = modifier
            .size(GRAPH_SIZE)
            .padding(horizontal = PrezelTheme.spacing.V4)
            .padding(top = PrezelTheme.spacing.V8),
    ) {
        SpeedGraphChart(
            userGauge = userGauge,
            goodRange = goodRange,
            baseRange = baseRange,
            colors = colors,
        )

        RangeBounds(
            lowerBound = baseRange.first,
            upperBound = baseRange.last,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun SpeedGraphChart(
    userGauge: Int,
    goodRange: IntRange,
    baseRange: IntRange,
    colors: SpeedGraphColors,
    modifier: Modifier = Modifier,
) {
    val hasValidBaseRange = baseRange.last > baseRange.first
    val clampedGauge = userGauge.coerceIn(baseRange.first, baseRange.last)
    val clampedGoodRange = goodRange.intersect(baseRange)
    val density = LocalDensity.current
    val startAngle = calculateStartAngle()
    val sweepAngle = FULL_CIRCLE_DEGREES
    val graphWidthPx = with(density) { GRAPH_DIAMETER.toPx() }
    val strokeWidthPx = (graphWidthPx / 2f) * GRAPH_STROKE_RATIO
    val arcDiameter = graphWidthPx - strokeWidthPx
    val arcTopLeft = Offset(
        x = (graphWidthPx - arcDiameter) / 2f,
        y = strokeWidthPx / 2f,
    )
    val arcSize = Size(width = arcDiameter, height = arcDiameter)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = colors.baseTrackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
            )

            if (hasValidBaseRange && !clampedGoodRange.isEmpty()) {
                drawArc(
                    color = colors.goodRangeColor,
                    startAngle = clampedGoodRange.first.toAngle(baseRange, startAngle, sweepAngle),
                    sweepAngle = clampedGoodRange.sweepAngle(baseRange),
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                )
            }

            if (hasValidBaseRange) {
                drawArc(
                    color = if (userGauge in goodRange) colors.goodGaugeColor else colors.badGaugeColor,
                    startAngle = startAngle,
                    sweepAngle = clampedGauge.sweepFromStart(baseRange),
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                )
            }
        }

        SpmDisplay(userGauge = userGauge)
    }
}

@Composable
private fun SpmDisplay(userGauge: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = userGauge.toString(),
            modifier = Modifier.height(32.dp),
            style = PrezelTheme.typography.title1Bold,
            color = PrezelTheme.colors.textRegular,
        )

        Text(
            text = "spm",
            modifier = Modifier.height(18.dp),
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelTheme.colors.textSmall,
        )
    }
}

@Composable
private fun RangeBounds(
    lowerBound: Int,
    upperBound: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GRAPH_LABEL_HORIZONTAL_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ProvideTextStyle(
            value = PrezelTheme.typography.caption1Regular.copy(color = PrezelTheme.colors.textSmall),
        ) {
            Text(text = lowerBound.toString())
            Text(text = upperBound.toString())
        }
    }
}

@BasicPreview
@Composable
private fun SpeedGraphPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SpeedGraph(userGauge = 190)
            SpeedGraph(userGauge = 220)
            SpeedGraph(userGauge = 270)
        }
    }
}

private fun Int.toAngle(
    baseRange: IntRange,
    startAngle: Float = calculateStartAngle(),
    sweepAngle: Float = FULL_CIRCLE_DEGREES,
): Float {
    if (baseRange.last <= baseRange.first) return startAngle

    val progress = (this - baseRange.first).toFloat() / (baseRange.last - baseRange.first).toFloat()
    return startAngle + (sweepAngle * progress.coerceIn(0f, 1f))
}

private fun Int.sweepFromStart(baseRange: IntRange): Float {
    val startAngle = calculateStartAngle()
    return toAngle(baseRange, startAngle = startAngle) - startAngle
}

private fun IntRange.sweepAngle(baseRange: IntRange): Float {
    if (isEmpty()) return 0f
    return max(last.toAngle(baseRange) - first.toAngle(baseRange), 0f)
}

private fun IntRange.intersect(other: IntRange): IntRange {
    val start = max(first, other.first)
    val endInclusive = minOf(last, other.last)
    return if (start <= endInclusive) IntRange(start, endInclusive) else IntRange.EMPTY
}

private fun calculateStartAngle(): Float {
    val gapSweep = 360f - FULL_CIRCLE_DEGREES
    return GAP_CENTER_ANGLE + (gapSweep / 2f)
}
