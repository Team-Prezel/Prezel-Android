package com.team.prezel.core.ui.component.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelVerticalDivider
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val SCROLL_THRESHOLD = 7
private val X_AXIS_LABEL_WIDTH = 28.dp
private const val CHART_DASH_STROKE_WIDTH = 1f
private const val CHART_BASELINE_STROKE_WIDTH = 2f
private val CHART_LINE_STROKE_WIDTH = 2.dp
private val CHART_SELECTED_GUIDE_STROKE_WIDTH = 1.dp
private val CHART_SELECTED_INNER_DOT_SIZE = 6.dp
private val CHART_SELECTED_OUTER_DOT_SIZE = 10.dp
private val CHART_SELECTED_TRIANGLE_WIDTH = 6.dp
private val CHART_SELECTED_TRIANGLE_HEIGHT = 6.dp
private val CARD_GRAPH_PREVIEW_WIDTH = 320.dp

data class CardGraphItem(
    val speech: Float,
    val scriptMatch: Float,
)

@Composable
fun CardGraph(
    items: ImmutableList<CardGraphItem>,
    modifier: Modifier = Modifier,
    selectedItemIndex: Int? = null,
    onSelectItem: (Int) -> Unit = {},
) {
    require(items.isNotEmpty()) {}
    val enableScroll = items.size > SCROLL_THRESHOLD
    val xAxisCenters = remember(items.size) {
        mutableStateListOf<Float>().apply {
            repeat(items.size) { add(Float.NaN) }
        }
    }

    BoxWithConstraints {
        val requireWidth = maxWidth
        val contentWidth = if (enableScroll) {
            X_AXIS_LABEL_WIDTH + ((requireWidth - X_AXIS_LABEL_WIDTH) / (SCROLL_THRESHOLD - 1)) * (items.size - 1)
        } else {
            requireWidth
        }

        Column(modifier = modifier.cardGraph()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (enableScroll) Modifier.horizontalScroll(rememberScrollState()) else Modifier,
                    ),
            ) {
                LinearChart(
                    items = items,
                    xAxisCenters = xAxisCenters,
                    selectedItemIndex = selectedItemIndex,
                    onSelectItem = onSelectItem,
                    modifier = Modifier
                        .width(contentWidth)
                        .weight(1f),
                )
                Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))
                XAxisRow(
                    size = items.size,
                    xAxisCenters = xAxisCenters,
                    onSelectItem = onSelectItem,
                    modifier = Modifier.width(contentWidth),
                )
                Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))
            }

            DetailContainer(
                items = items,
                selectedItemIndex = selectedItemIndex,
            )
        }
    }
}

@Composable
private fun Modifier.cardGraph(): Modifier =
    this
        .fillMaxWidth()
        .aspectRatio(320 / 212f)
        .clip(PrezelTheme.shapes.V8)
        .background(color = PrezelTheme.colors.bgMedium)
        .padding(
            vertical = PrezelTheme.spacing.V12,
            horizontal = PrezelTheme.spacing.V16,
        )

@Composable
private fun LinearChart(
    items: ImmutableList<CardGraphItem>,
    xAxisCenters: List<Float>,
    selectedItemIndex: Int?,
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dashColor = PrezelTheme.colors.borderSmall
    val underlineColor = PrezelTheme.colors.borderRegular
    val speechColor = PrezelTheme.colors.feedbackGoodRegular
    val scriptMatchColor = PrezelTheme.colors.feedbackWarningRegular
    val selectedGuideColor = PrezelTheme.colors.borderMedium
    val density = LocalDensity.current
    val lineStrokeWidthPx = with(density) { CHART_LINE_STROKE_WIDTH.toPx() }
    val selectedGuideStrokeWidthPx = with(density) { CHART_SELECTED_GUIDE_STROKE_WIDTH.toPx() }
    val innerDotRadiusPx = with(density) { CHART_SELECTED_INNER_DOT_SIZE.toPx() / 2f }
    val outerDotRadiusPx = with(density) { CHART_SELECTED_OUTER_DOT_SIZE.toPx() / 2f }
    val selectedTriangleWidthPx = with(density) { CHART_SELECTED_TRIANGLE_WIDTH.toPx() }
    val selectedTriangleHeightPx = with(density) { CHART_SELECTED_TRIANGLE_HEIGHT.toPx() }

    Box(
        modifier = modifier.pointerInput(xAxisCenters, items.size) {
            detectTapGestures { tapOffset ->
                xAxisCenters.findClosestIndex(tapOffset.x)?.let(onSelectItem)
            }
        },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val baselineY = size.height - (CHART_BASELINE_STROKE_WIDTH / 2f)
            val chartHeight = baselineY
            val speechOffsets = items.mapIndexedNotNull { index, item ->
                xAxisCenters
                    .getOrNull(index)
                    ?.takeUnless(Float::isNaN)
                    ?.let { centerX ->
                        Offset(
                            x = centerX,
                            y = chartHeight - (item.speech.coerceIn(0f, 1f) * chartHeight),
                        )
                    }
            }
            val scriptMatchOffsets = items.mapIndexedNotNull { index, item ->
                xAxisCenters
                    .getOrNull(index)
                    ?.takeUnless(Float::isNaN)
                    ?.let { centerX ->
                        Offset(
                            x = centerX,
                            y = chartHeight - (item.scriptMatch.coerceIn(0f, 1f) * chartHeight),
                        )
                    }
            }

            repeat(items.size) { index ->
                val centerX = xAxisCenters.getOrNull(index)
                if (centerX != null && !centerX.isNaN()) {
                    drawLine(
                        color = dashColor,
                        start = Offset(x = centerX, y = 0f),
                        end = Offset(x = centerX, y = size.height),
                        strokeWidth = CHART_DASH_STROKE_WIDTH,
                        cap = StrokeCap.Butt,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(1f, 2f),
                        ),
                    )
                }
            }

            drawLine(
                color = underlineColor,
                start = Offset(x = 0f, y = baselineY),
                end = Offset(x = size.width, y = baselineY),
                strokeWidth = CHART_BASELINE_STROKE_WIDTH,
            )

            if (selectedItemIndex != null) {
                drawSelectedGuide(
                    xAxisCenters = xAxisCenters,
                    selectedIndex = selectedItemIndex,
                    color = selectedGuideColor,
                    baselineY = baselineY,
                    strokeWidth = selectedGuideStrokeWidthPx,
                    triangleWidth = selectedTriangleWidthPx,
                    triangleHeight = selectedTriangleHeightPx,
                )
            }

            drawSeriesLine(
                points = speechOffsets,
                color = speechColor,
                strokeWidth = lineStrokeWidthPx,
            )
            drawSeriesLine(
                points = scriptMatchOffsets,
                color = scriptMatchColor,
                strokeWidth = lineStrokeWidthPx,
            )

            if (selectedItemIndex != null) {
                drawSelectedMarker(
                    points = speechOffsets,
                    selectedIndex = selectedItemIndex,
                    color = speechColor,
                    outerRadius = outerDotRadiusPx,
                    innerRadius = innerDotRadiusPx,
                )
                drawSelectedMarker(
                    points = scriptMatchOffsets,
                    selectedIndex = selectedItemIndex,
                    color = scriptMatchColor,
                    outerRadius = outerDotRadiusPx,
                    innerRadius = innerDotRadiusPx,
                )
            }
        }
    }
}

@Composable
private fun XAxisRow(
    size: Int,
    xAxisCenters: MutableList<Float>,
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(size) { index ->
            Text(
                text = "${index + 1}차",
                style = PrezelTheme.typography.caption2Regular,
                color = PrezelTheme.colors.textSmall,
                modifier = Modifier
                    .width(X_AXIS_LABEL_WIDTH)
                    .noRippleClickable { onSelectItem(index) }
                    .onGloballyPositioned { coordinates ->
                        xAxisCenters[index] = coordinates.positionInParent().x + (coordinates.size.width / 2f)
                    },
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DetailContainer(
    items: ImmutableList<CardGraphItem>,
    selectedItemIndex: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        LegendItem(
            label = "발화",
            color = PrezelTheme.colors.feedbackGoodRegular,
            valueText = items.toDetailValueText(
                selectedItemIndex = selectedItemIndex,
                valueSelector = CardGraphItem::speech,
            ),
            modifier = Modifier.weight(1f),
        )
        PrezelVerticalDivider(
            type = PrezelDividerType.THICK,
            color = PrezelTheme.colors.borderRegular,
            modifier = Modifier.fillMaxHeight(),
        )
        LegendItem(
            label = "대본 일치율",
            color = PrezelTheme.colors.feedbackWarningRegular,
            valueText = items.toDetailValueText(
                selectedItemIndex = selectedItemIndex,
                valueSelector = CardGraphItem::scriptMatch,
            ),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun LegendItem(
    label: String,
    color: Color,
    valueText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = PrezelTheme.spacing.V8,
                vertical = PrezelTheme.spacing.V6,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(PrezelTheme.shapes.V1000)
                    .size(8.dp, 2.dp)
                    .background(color),
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))
            Text(
                text = label,
                style = PrezelTheme.typography.caption2Regular,
                color = color,
            )
        }

        Text(
            text = valueText,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textMedium,
        )
    }
}

private fun List<CardGraphItem>.toDetailValueText(
    selectedItemIndex: Int?,
    valueSelector: (CardGraphItem) -> Float,
): String {
    val selectedItem = selectedItemIndex?.let(::getOrNull)
    return if (selectedItem != null) {
        selectedItem.toPercentText(valueSelector)
    } else {
        val pointDiff = valueSelector(last()) - valueSelector(first())
        pointDiff.toPercentPointText()
    }
}

private fun CardGraphItem.toPercentText(valueSelector: (CardGraphItem) -> Float): String = "${(valueSelector(this).coerceIn(0f, 1f) * 100).toInt()}%"

private fun Float.toPercentPointText(): String {
    val value = (this * 100).toInt()
    val prefix = if (value > 0) "+" else ""
    return "${prefix}$value%p"
}

private fun DrawScope.drawSeriesLine(
    points: List<Offset>,
    color: Color,
    strokeWidth: Float,
) {
    if (points.size < 2) return

    for (index in 0 until points.lastIndex) {
        drawLine(
            color = color,
            start = points[index],
            end = points[index + 1],
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawSelectedMarker(
    points: List<Offset>,
    selectedIndex: Int,
    color: Color,
    outerRadius: Float,
    innerRadius: Float,
) {
    val point = points.getOrNull(selectedIndex) ?: return

    drawCircle(
        color = color.copy(alpha = 0.3f),
        radius = outerRadius,
        center = point,
    )
    drawCircle(
        color = color,
        radius = innerRadius,
        center = point,
    )
}

private fun DrawScope.drawSelectedGuide(
    xAxisCenters: List<Float>,
    selectedIndex: Int,
    color: Color,
    baselineY: Float,
    strokeWidth: Float,
    triangleWidth: Float,
    triangleHeight: Float,
) {
    val centerX = xAxisCenters.getOrNull(selectedIndex)?.takeUnless(Float::isNaN) ?: return
    val triangleApexY = baselineY - triangleHeight

    drawLine(
        color = color,
        start = Offset(x = centerX, y = 0f),
        end = Offset(x = centerX, y = triangleApexY),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Butt,
    )

    drawPath(
        path = Path().apply {
            moveTo(x = centerX - (triangleWidth / 2f), y = baselineY)
            lineTo(x = centerX + (triangleWidth / 2f), y = baselineY)
            lineTo(x = centerX, y = triangleApexY)
            close()
        },
        color = color,
    )
}

private fun List<Float>.findClosestIndex(targetX: Float): Int? =
    mapIndexedNotNull { index, centerX ->
        centerX.takeUnless(Float::isNaN)?.let { index to kotlin.math.abs(it - targetX) }
    }.minByOrNull { (_, distance) -> distance }
        ?.first

private val cardGraphPreviewItems = persistentListOf(
    CardGraphItem(
        speech = 0.92f,
        scriptMatch = 0.88f,
    ),
    CardGraphItem(
        speech = 0.75f,
        scriptMatch = 0.81f,
    ),
    CardGraphItem(
        speech = 0.63f,
        scriptMatch = 0.58f,
    ),
    CardGraphItem(
        speech = 0.48f,
        scriptMatch = 0.71f,
    ),
    CardGraphItem(
        speech = 0.84f,
        scriptMatch = 0.79f,
    ),
    CardGraphItem(
        speech = 0.56f,
        scriptMatch = 0.64f,
    ),
    CardGraphItem(
        speech = 0.97f,
        scriptMatch = 0.91f,
    ),
    CardGraphItem(
        speech = 0.69f,
        scriptMatch = 0.73f,
    ),
)

private val cardGraphCompactPreviewItems = persistentListOf(
    CardGraphItem(
        speech = 0.92f,
        scriptMatch = 0.88f,
    ),
    CardGraphItem(
        speech = 0.75f,
        scriptMatch = 0.81f,
    ),
    CardGraphItem(
        speech = 0.63f,
        scriptMatch = 0.58f,
    ),
    CardGraphItem(
        speech = 0.48f,
        scriptMatch = 0.71f,
    ),
    CardGraphItem(
        speech = 0.84f,
        scriptMatch = 0.79f,
    ),
)

@Composable
private fun CardGraphPreviewContainer(
    items: ImmutableList<CardGraphItem>,
    selectedItemIndex: Int? = null,
) {
    Box(
        modifier = Modifier
            .width(CARD_GRAPH_PREVIEW_WIDTH)
            .padding(12.dp),
    ) {
        CardGraph(
            items = items,
            selectedItemIndex = selectedItemIndex,
        )
    }
}

@BasicPreview
@Composable
private fun CardGraphDefaultPreview() {
    PrezelTheme {
        CardGraphPreviewContainer(items = cardGraphCompactPreviewItems)
    }
}

@BasicPreview
@Composable
private fun CardGraphSelectedPointPreview() {
    PrezelTheme {
        CardGraphPreviewContainer(
            items = cardGraphCompactPreviewItems,
            selectedItemIndex = 2,
        )
    }
}

@BasicPreview
@Composable
private fun CardGraphScrollablePreview() {
    PrezelTheme {
        CardGraphPreviewContainer(items = cardGraphPreviewItems)
    }
}

@BasicPreview
@Composable
private fun CardGraphScrollableSelectedPointPreview() {
    PrezelTheme {
        CardGraphPreviewContainer(
            items = cardGraphPreviewItems,
            selectedItemIndex = 6,
        )
    }
}

@BasicPreview
@Composable
private fun CardGraphInteractivePreview() {
    PrezelTheme {
        var selectedItemIndex by remember { mutableStateOf<Int?>(2) }

        Box(
            modifier = Modifier
                .width(CARD_GRAPH_PREVIEW_WIDTH)
                .padding(12.dp),
        ) {
            CardGraph(
                items = cardGraphPreviewItems,
                selectedItemIndex = selectedItemIndex,
                onSelectItem = { index ->
                    selectedItemIndex = if (selectedItemIndex == index) null else index
                },
            )
        }
    }
}
