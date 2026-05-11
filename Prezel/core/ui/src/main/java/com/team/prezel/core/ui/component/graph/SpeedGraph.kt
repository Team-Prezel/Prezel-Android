package com.team.prezel.core.ui.component.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlin.math.max
import kotlin.math.min

private const val DEFAULT_LOWER_BOUND = 180
private const val DEFAULT_UPPER_BOUND = 280
private const val DEFAULT_GOOD_LOWER_BOUND = 210
private const val DEFAULT_GOOD_UPPER_BOUND = 260

private const val FULL_CIRCLE_DEGREES = 270f
private const val START_ANGLE = 135f
private const val GRAPH_STROKE_RATIO = 0.12f
private const val GRAPH_LABEL_HORIZONTAL_PADDING_RATIO = 0.1875f
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
    BoxWithConstraints(
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
            horizontalPadding = maxWidth * GRAPH_LABEL_HORIZONTAL_PADDING_RATIO,
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
    val clampedGoodRange = goodRange.intersect(baseRange)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val graphWidthPx = min(size.width, size.height)
            val strokeWidthPx = (graphWidthPx / 2f) * GRAPH_STROKE_RATIO
            val arcDiameter = graphWidthPx - strokeWidthPx
            val arcTopLeft = Offset(
                x = (size.width - arcDiameter) / 2f,
                y = (size.height - arcDiameter) / 2f,
            )
            val arcSize = Size(width = arcDiameter, height = arcDiameter)
            val arcStroke = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)

            drawArc(
                color = colors.baseTrackColor,
                startAngle = START_ANGLE,
                sweepAngle = FULL_CIRCLE_DEGREES,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = arcStroke,
            )

            if (hasValidBaseRange && !clampedGoodRange.isEmpty()) {
                drawArc(
                    color = colors.goodRangeColor,
                    startAngle = clampedGoodRange.first.toAngle(baseRange),
                    sweepAngle = clampedGoodRange.sweepAngle(baseRange),
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = arcStroke,
                )
            }

            if (hasValidBaseRange) {
                drawArc(
                    color = if (userGauge in goodRange) colors.goodGaugeColor else colors.badGaugeColor,
                    startAngle = START_ANGLE,
                    sweepAngle = userGauge.sweepFromStart(baseRange = baseRange),
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = arcStroke,
                )
            }
        }

        SpmDisplay(userGauge = userGauge)
    }
}

/** 현재 속도 값과 단위를 그래프 중앙에 표시합니다. */
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

/** 기준 범위의 시작값과 끝값을 그래프 하단에 배치합니다. */
@Composable
private fun RangeBounds(
    lowerBound: Int,
    upperBound: Int,
    horizontalPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
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

/** 기준 범위 안의 값을 그래프 각도로 변환합니다. */
private fun Int.toAngle(baseRange: IntRange): Float {
    if (baseRange.last <= baseRange.first) return START_ANGLE

    val progress = (this - baseRange.first).toFloat() / (baseRange.last - baseRange.first).toFloat()
    return START_ANGLE + (FULL_CIRCLE_DEGREES * progress.coerceIn(0f, 1f))
}

/** 그래프 시작점부터 현재 값까지의 sweep 각도를 계산합니다. */
private fun Int.sweepFromStart(baseRange: IntRange): Float = toAngle(baseRange) - START_ANGLE

/** 기준 범위 안에서 겹치는 구간의 sweep 각도를 계산합니다. */
private fun IntRange.sweepAngle(baseRange: IntRange): Float {
    if (isEmpty()) return 0f
    return max(
        last.toAngle(baseRange) - first.toAngle(baseRange),
        0f,
    )
}

/** 두 범위가 겹치는 구간만 남깁니다. */
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
