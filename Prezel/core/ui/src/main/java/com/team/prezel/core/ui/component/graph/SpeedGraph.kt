package com.team.prezel.core.ui.component.graph

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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlin.math.max
import kotlin.math.min

private const val DEFAULT_LOWER_BOUND = 190
private const val DEFAULT_UPPER_BOUND = 280
private const val DEFAULT_GOOD_LOWER_BOUND = 210
private const val DEFAULT_GOOD_UPPER_BOUND = 260

private const val FULL_CIRCLE_DEGREES = 270f
private const val START_ANGLE = 135f
private const val GRAPH_STROKE_RATIO = 0.12f
private const val GRAPH_LABEL_CONTENT_WIDTH_RATIO = 0.625f
private val GRAPH_SIZE = 160.dp

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
    goodRange: IntRange = IntRange(DEFAULT_GOOD_LOWER_BOUND, DEFAULT_GOOD_UPPER_BOUND),
    baseRange: IntRange = IntRange(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND),
    colors: SpeedGraphColors = SpeedGraphColors.getDefault(),
) {
    Box(
        modifier = modifier
            .size(GRAPH_SIZE)
            .padding(horizontal = PrezelTheme.spacing.V4)
            .padding(top = PrezelTheme.spacing.V8),
    ) {
        SpeedGraphContent(
            userGauge = userGauge,
            goodRange = goodRange,
            baseRange = baseRange,
            colors = colors,
        )

        RangeBoundLabels(
            lowerBound = baseRange.first,
            upperBound = baseRange.last,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun SpeedGraphContent(
    userGauge: Int,
    goodRange: IntRange,
    baseRange: IntRange,
    colors: SpeedGraphColors,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        SpeedGaugeArc(
            userGauge = userGauge,
            goodRange = goodRange,
            baseRange = baseRange,
            colors = colors,
        )

        SpeedValueLabel(userGauge = userGauge)
    }
}

@Composable
private fun SpeedGaugeArc(
    userGauge: Int,
    goodRange: IntRange,
    baseRange: IntRange,
    colors: SpeedGraphColors,
) {
    val clampedGoodRange = goodRange.intersect(baseRange)
    val goodRangeStartAngle = clampedGoodRange.first.toGraphAngle(baseRange)
    val goodRangeSweepAngle = clampedGoodRange.graphSweepAngle(baseRange)
    val userGaugeSweepAngle = userGauge.graphSweepAngleFromStart(baseRange)
    val userGaugeColor = if (userGauge in goodRange) colors.goodGaugeColor else colors.badGaugeColor

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val (arcTopLeft: Offset, arcSize: Size, arcStroke: Stroke) = size.calculateGraphArcMetrics()

                onDrawBehind {
                    drawBaseTrack(
                        trackColor = colors.baseTrackColor,
                        arcTopLeft = arcTopLeft,
                        arcSize = arcSize,
                        arcStroke = arcStroke,
                    )

                    if (!clampedGoodRange.isEmpty()) {
                        drawGoodRange(
                            rangeColor = colors.goodRangeColor,
                            goodRangeStartAngle = goodRangeStartAngle,
                            goodRangeSweepAngle = goodRangeSweepAngle,
                            arcTopLeft = arcTopLeft,
                            arcSize = arcSize,
                            arcStroke = arcStroke,
                        )
                    }

                    drawUserGauge(
                        userGaugeColor = userGaugeColor,
                        userGaugeSweepAngle = userGaugeSweepAngle,
                        arcTopLeft = arcTopLeft,
                        arcSize = arcSize,
                        arcStroke = arcStroke,
                    )
                }
            },
    )
}

private fun DrawScope.drawBaseTrack(
    trackColor: Color,
    arcTopLeft: Offset,
    arcSize: Size,
    arcStroke: Stroke,
) {
    drawArc(
        color = trackColor,
        startAngle = START_ANGLE,
        sweepAngle = FULL_CIRCLE_DEGREES,
        useCenter = false,
        topLeft = arcTopLeft,
        size = arcSize,
        style = arcStroke,
    )
}

private fun DrawScope.drawGoodRange(
    rangeColor: Color,
    goodRangeStartAngle: Float,
    goodRangeSweepAngle: Float,
    arcTopLeft: Offset,
    arcSize: Size,
    arcStroke: Stroke,
) {
    drawArc(
        color = rangeColor,
        startAngle = goodRangeStartAngle,
        sweepAngle = goodRangeSweepAngle,
        useCenter = false,
        topLeft = arcTopLeft,
        size = arcSize,
        style = arcStroke,
    )
}

private fun DrawScope.drawUserGauge(
    userGaugeColor: Color,
    userGaugeSweepAngle: Float,
    arcTopLeft: Offset,
    arcSize: Size,
    arcStroke: Stroke,
) {
    drawArc(
        color = userGaugeColor,
        startAngle = START_ANGLE,
        sweepAngle = userGaugeSweepAngle,
        useCenter = false,
        topLeft = arcTopLeft,
        size = arcSize,
        style = arcStroke,
    )
}

@Composable
private fun SpeedValueLabel(userGauge: Int) {
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
private fun RangeBoundLabels(
    lowerBound: Int,
    upperBound: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(GRAPH_LABEL_CONTENT_WIDTH_RATIO),
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

private fun Size.calculateGraphArcMetrics(): Triple<Offset, Size, Stroke> {
    val graphWidthPx = min(width, height)
    val strokeWidthPx = (graphWidthPx / 2f) * GRAPH_STROKE_RATIO
    val arcDiameter = graphWidthPx - strokeWidthPx

    return Triple(
        first = Offset(
            x = (width - arcDiameter) / 2f,
            y = (height - arcDiameter) / 2f,
        ),
        second = Size(width = arcDiameter, height = arcDiameter),
        third = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
    )
}

private fun Int.toGraphAngle(baseRange: IntRange): Float {
    if (baseRange.last <= baseRange.first) return START_ANGLE

    val progress = (this - baseRange.first).toFloat() / (baseRange.last - baseRange.first).toFloat()
    return START_ANGLE + (FULL_CIRCLE_DEGREES * progress.coerceIn(0f, 1f))
}

private fun Int.graphSweepAngleFromStart(baseRange: IntRange): Float = toGraphAngle(baseRange) - START_ANGLE

private fun IntRange.graphSweepAngle(baseRange: IntRange): Float {
    if (isEmpty()) return 0f
    return max(
        last.toGraphAngle(baseRange) - first.toGraphAngle(baseRange),
        0f,
    )
}

private fun IntRange.intersect(other: IntRange): IntRange {
    val start = max(first, other.first)
    val endInclusive = minOf(last, other.last)
    return if (start <= endInclusive) IntRange(start, endInclusive) else IntRange.EMPTY
}

@BasicPreview
@Composable
private fun SpeedGraphGoodPreview() {
    PrezelTheme {
        SpeedGraph(
            userGauge = 241,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@BasicPreview
@Composable
private fun SpeedGraphSlowPreview() {
    PrezelTheme {
        SpeedGraph(
            userGauge = 190,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@BasicPreview
@Composable
private fun SpeedGraphFastPreview() {
    PrezelTheme {
        SpeedGraph(
            userGauge = 270,
            modifier = Modifier.padding(16.dp),
        )
    }
}
