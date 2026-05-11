package com.team.prezel.core.ui.component.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelVerticalDivider
import com.team.prezel.core.designsystem.component.chip.chip.ChipState
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.R
import com.team.prezel.core.ui.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

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
private const val CARD_GRAPH_ASPECT_RATIO = 1.5f

private data class CardGraphUiState(
    val enableScroll: Boolean,
    val contentWidth: Dp,
    val xAxisCenters: ImmutableList<Float>,
    val selectedItemIndex: Int?,
)

private data class CardGraphColors(
    val dash: Color,
    val baseline: Color,
    val speech: Color,
    val scriptMatch: Color,
    val selectedGuide: Color,
)

private data class CardGraphDimensions(
    val lineStrokeWidthPx: Float,
    val selectedGuideStrokeWidthPx: Float,
    val innerDotRadiusPx: Float,
    val outerDotRadiusPx: Float,
    val selectedTriangleWidthPx: Float,
    val selectedTriangleHeightPx: Float,
)

private data class CardGraphSeriesPoints(
    val speech: List<Offset>,
    val scriptMatch: List<Offset>,
)

private data class CardGraphChartState(
    val baselineY: Float,
    val seriesPoints: CardGraphSeriesPoints,
    val validXAxisCenters: List<Float>,
    val selectedItemIndex: Int?,
)

data class CardGraphItem(
    val speech: Float,
    val scriptMatch: Float,
)

@Composable
fun CardGraph(
    items: ImmutableList<CardGraphItem>,
    modifier: Modifier = Modifier,
    selectedItemIndex: Int? = null,
    showDetail: Boolean = true,
    useContainerStyle: Boolean = true,
    onSelectItem: (Int) -> Unit = {},
) {
    require(items.isNotEmpty()) { "CardGraph items must not be empty." }

    val xAxisCenters = rememberCardGraphXAxisCenters(itemCount = items.size)

    BoxWithConstraints {
        val uiState = CardGraphUiState(
            enableScroll = items.shouldEnableHorizontalScroll(),
            contentWidth = maxWidth.toChartContentWidth(itemCount = items.size),
            xAxisCenters = xAxisCenters.toImmutableList(),
            selectedItemIndex = items.resolveSelectedItemIndex(selectedItemIndex),
        )

        CardGraphContainer(
            modifier = modifier,
            useContainerStyle = useContainerStyle,
        ) {
            CardGraphContent(
                items = items,
                uiState = uiState,
                onChangePosition = { index, center ->
                    xAxisCenters[index] = center
                },
                onSelectItem = onSelectItem,
                modifier = Modifier.weight(1f, fill = false),
            )

            if (showDetail) {
                DetailContainer(
                    items = items,
                    selectedItemIndex = uiState.selectedItemIndex,
                )
            }
        }
    }
}

@Composable
private fun CardGraphContainer(
    useContainerStyle: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(CARD_GRAPH_ASPECT_RATIO)
            .then(
                if (useContainerStyle) {
                    Modifier
                        .clip(PrezelTheme.shapes.V8)
                        .background(color = PrezelTheme.colors.bgMedium)
                        .padding(
                            vertical = PrezelTheme.spacing.V12,
                            horizontal = PrezelTheme.spacing.V16,
                        )
                } else {
                    Modifier
                },
            ),
        content = content,
    )
}

@Composable
private fun CardGraphContent(
    items: ImmutableList<CardGraphItem>,
    uiState: CardGraphUiState,
    onChangePosition: (index: Int, center: Float) -> Unit,
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .then(
                if (uiState.enableScroll) {
                    Modifier.horizontalScroll(
                        state = rememberScrollState(),
                        overscrollEffect = null,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        LinearChart(
            items = items,
            uiState = uiState,
            onSelectItem = onSelectItem,
            modifier = Modifier
                .width(uiState.contentWidth)
                .weight(1f, fill = false),
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))
        XAxisRow(
            size = items.size,
            onSelectItem = onSelectItem,
            modifier = Modifier.width(uiState.contentWidth),
            onChangePosition = onChangePosition,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))
    }
}

@Composable
private fun LinearChart(
    items: ImmutableList<CardGraphItem>,
    uiState: CardGraphUiState,
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = cardGraphColors()
    val dimensions = rememberCardGraphDimensions()

    Box(
        modifier = modifier.pointerInput(uiState.xAxisCenters, items.size) {
            detectTapGestures { tapOffset ->
                with(CardGraphMath) {
                    uiState.xAxisCenters.findClosestIndex(tapOffset.x)?.let(onSelectItem)
                }
            }
        },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val chartState = with(CardGraphMath) {
                items.toChartState(
                    xAxisCenters = uiState.xAxisCenters,
                    chartHeight = size.height,
                    selectedItemIndex = uiState.selectedItemIndex,
                )
            }
            with(CardGraphDrawers) {
                drawChart(
                    chartState = chartState,
                    xAxisCenters = uiState.xAxisCenters,
                    colors = colors,
                    dimensions = dimensions,
                )
            }
        }
    }
}

@Composable
private fun XAxisRow(
    size: Int,
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onChangePosition: (index: Int, center: Float) -> Unit,
) {
    val horizontalArrangement = if (size == 1) Arrangement.Center else Arrangement.SpaceBetween

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        repeat(size) { index ->
            Text(
                text = stringResource(
                    id = R.string.core_ui_impl_card_graph_x_axis_label,
                    index + 1,
                ),
                style = PrezelTheme.typography.caption2Regular,
                color = PrezelTheme.colors.textSmall,
                modifier = Modifier
                    .width(X_AXIS_LABEL_WIDTH)
                    .noRippleClickable { onSelectItem(index) }
                    .onGloballyPositioned { coordinates ->
                        val center = coordinates.positionInParent().x + (coordinates.size.width / 2f)
                        onChangePosition(index, center)
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
            label = stringResource(R.string.core_ui_impl_card_graph_speech_label),
            color = PrezelTheme.colors.feedbackGoodRegular,
            valueText = with(CardGraphTextFormatter) {
                items.toDetailValueText(
                    selectedItemIndex = selectedItemIndex,
                    valueSelector = CardGraphItem::speech,
                )
            },
            modifier = Modifier.weight(1f),
        )
        PrezelVerticalDivider(
            type = PrezelDividerType.THICK,
            color = PrezelTheme.colors.borderRegular,
            modifier = Modifier.fillMaxHeight(),
        )
        LegendItem(
            label = stringResource(R.string.core_ui_impl_card_graph_script_match_label),
            color = PrezelTheme.colors.feedbackWarningRegular,
            valueText = with(CardGraphTextFormatter) {
                items.toDetailValueText(
                    selectedItemIndex = selectedItemIndex,
                    valueSelector = CardGraphItem::scriptMatch,
                )
            },
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

@Composable
private fun cardGraphColors(): CardGraphColors =
    CardGraphColors(
        dash = PrezelTheme.colors.borderSmall,
        baseline = PrezelTheme.colors.borderRegular,
        speech = PrezelTheme.colors.feedbackGoodRegular,
        scriptMatch = PrezelTheme.colors.feedbackWarningRegular,
        selectedGuide = PrezelTheme.colors.borderMedium,
    )

@Composable
private fun rememberCardGraphDimensions(): CardGraphDimensions {
    val density = LocalDensity.current
    return with(density) {
        CardGraphDimensions(
            lineStrokeWidthPx = CHART_LINE_STROKE_WIDTH.toPx(),
            selectedGuideStrokeWidthPx = CHART_SELECTED_GUIDE_STROKE_WIDTH.toPx(),
            innerDotRadiusPx = CHART_SELECTED_INNER_DOT_SIZE.toPx() / 2f,
            outerDotRadiusPx = CHART_SELECTED_OUTER_DOT_SIZE.toPx() / 2f,
            selectedTriangleWidthPx = CHART_SELECTED_TRIANGLE_WIDTH.toPx(),
            selectedTriangleHeightPx = CHART_SELECTED_TRIANGLE_HEIGHT.toPx(),
        )
    }
}

@Composable
private fun rememberCardGraphXAxisCenters(itemCount: Int) =
    remember(itemCount) {
        mutableStateListOf<Float>().apply {
            repeat(itemCount) { add(Float.NaN) }
        }
    }

private fun ImmutableList<CardGraphItem>.shouldEnableHorizontalScroll(): Boolean = size > SCROLL_THRESHOLD

private fun List<CardGraphItem>.resolveSelectedItemIndex(selectedItemIndex: Int?): Int? =
    selectedItemIndex.takeIf { index -> index != null && index in indices }

private fun Dp.toChartContentWidth(itemCount: Int): Dp =
    if (itemCount > SCROLL_THRESHOLD) {
        X_AXIS_LABEL_WIDTH + ((this - X_AXIS_LABEL_WIDTH) / (SCROLL_THRESHOLD - 1)) * (itemCount - 1)
    } else {
        this
    }

private object CardGraphTextFormatter {
    fun List<CardGraphItem>.toDetailValueText(
        selectedItemIndex: Int?,
        valueSelector: (CardGraphItem) -> Float,
    ): String {
        val selectedItem = selectedItemIndex?.let(::getOrNull)
        return if (selectedItem != null) {
            selectedItem.toPercentText(valueSelector)
        } else if (size == 1) {
            first().toPercentText(valueSelector)
        } else {
            valueSelector(last()).minus(valueSelector(first())).toPercentPointText()
        }
    }

    private fun CardGraphItem.toPercentText(valueSelector: (CardGraphItem) -> Float): String =
        "${(valueSelector(this).coerceIn(0f, 1f) * 100).toInt()}%"

    private fun Float.toPercentPointText(): String {
        val value = (this * 100).toInt()
        val prefix = if (value > 0) "+" else ""
        return "${prefix}$value%p"
    }
}

private object CardGraphMath {
    fun List<CardGraphItem>.toChartState(
        xAxisCenters: List<Float>,
        chartHeight: Float,
        selectedItemIndex: Int?,
    ): CardGraphChartState {
        val baselineY = chartHeight - (CHART_BASELINE_STROKE_WIDTH / 2f)
        return CardGraphChartState(
            baselineY = baselineY,
            seriesPoints = toSeriesPoints(
                xAxisCenters = xAxisCenters,
                chartHeight = baselineY,
            ),
            validXAxisCenters = xAxisCenters.validCenters(),
            selectedItemIndex = selectedItemIndex,
        )
    }

    fun List<Float>.findClosestIndex(targetX: Float): Int? =
        mapIndexedNotNull { index, centerX ->
            centerX.validCenterOrNull()?.let { index to kotlin.math.abs(it - targetX) }
        }.minByOrNull { (_, distance) -> distance }
            ?.first

    private fun List<Float>.validCenters(): List<Float> = mapNotNull { it.validCenterOrNull() }

    private fun Float.validCenterOrNull(): Float? = takeUnless(Float::isNaN)

    private fun List<CardGraphItem>.toSeriesPoints(
        xAxisCenters: List<Float>,
        chartHeight: Float,
    ): CardGraphSeriesPoints =
        CardGraphSeriesPoints(
            speech = mapSeriesPoints(
                xAxisCenters = xAxisCenters,
                chartHeight = chartHeight,
                valueSelector = CardGraphItem::speech,
            ),
            scriptMatch = mapSeriesPoints(
                xAxisCenters = xAxisCenters,
                chartHeight = chartHeight,
                valueSelector = CardGraphItem::scriptMatch,
            ),
        )

    private fun List<CardGraphItem>.mapSeriesPoints(
        xAxisCenters: List<Float>,
        chartHeight: Float,
        valueSelector: (CardGraphItem) -> Float,
    ): List<Offset> =
        mapIndexedNotNull { index, item ->
            xAxisCenters
                .getOrNull(index)
                ?.validCenterOrNull()
                ?.let { centerX ->
                    Offset(
                        x = centerX,
                        y = chartHeight - (valueSelector(item).coerceIn(0f, 1f) * chartHeight),
                    )
                }
        }
}

private object CardGraphDrawers {
    fun DrawScope.drawChart(
        chartState: CardGraphChartState,
        xAxisCenters: List<Float>,
        colors: CardGraphColors,
        dimensions: CardGraphDimensions,
    ) {
        drawVerticalGuides(chartState.validXAxisCenters, colors.dash)
        drawBaseline(chartState.baselineY, colors.baseline)
        drawSelectionGuide(chartState, xAxisCenters, colors, dimensions)
        drawSeries(chartState, colors, dimensions)
        drawSinglePointMarkers(chartState, colors, dimensions)
        drawSelectionMarkers(chartState, colors, dimensions)
    }

    private fun DrawScope.drawVerticalGuides(
        validXAxisCenters: List<Float>,
        color: Color,
    ) {
        validXAxisCenters.forEach { centerX ->
            drawLine(
                color = color,
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

    private fun DrawScope.drawBaseline(
        baselineY: Float,
        color: Color,
    ) {
        drawLine(
            color = color,
            start = Offset(x = 0f, y = baselineY),
            end = Offset(x = size.width, y = baselineY),
            strokeWidth = CHART_BASELINE_STROKE_WIDTH,
        )
    }

    private fun DrawScope.drawSelectionGuide(
        chartState: CardGraphChartState,
        xAxisCenters: List<Float>,
        colors: CardGraphColors,
        dimensions: CardGraphDimensions,
    ) {
        val selectedIndex = chartState.selectedItemIndex ?: return
        drawSelectedGuide(
            xAxisCenters = xAxisCenters,
            selectedIndex = selectedIndex,
            color = colors.selectedGuide,
            baselineY = chartState.baselineY,
            strokeWidth = dimensions.selectedGuideStrokeWidthPx,
            triangleWidth = dimensions.selectedTriangleWidthPx,
            triangleHeight = dimensions.selectedTriangleHeightPx,
        )
    }

    private fun DrawScope.drawSeries(
        chartState: CardGraphChartState,
        colors: CardGraphColors,
        dimensions: CardGraphDimensions,
    ) {
        drawSeriesLine(
            points = chartState.seriesPoints.speech,
            color = colors.speech,
            strokeWidth = dimensions.lineStrokeWidthPx,
        )
        drawSeriesLine(
            points = chartState.seriesPoints.scriptMatch,
            color = colors.scriptMatch,
            strokeWidth = dimensions.lineStrokeWidthPx,
        )
    }

    private fun DrawScope.drawSelectionMarkers(
        chartState: CardGraphChartState,
        colors: CardGraphColors,
        dimensions: CardGraphDimensions,
    ) {
        val selectedIndex = chartState.selectedItemIndex ?: return
        drawSelectedMarker(
            points = chartState.seriesPoints.speech,
            selectedIndex = selectedIndex,
            color = colors.speech,
            outerRadius = dimensions.outerDotRadiusPx,
            innerRadius = dimensions.innerDotRadiusPx,
        )
        drawSelectedMarker(
            points = chartState.seriesPoints.scriptMatch,
            selectedIndex = selectedIndex,
            color = colors.scriptMatch,
            outerRadius = dimensions.outerDotRadiusPx,
            innerRadius = dimensions.innerDotRadiusPx,
        )
    }

    private fun DrawScope.drawSinglePointMarkers(
        chartState: CardGraphChartState,
        colors: CardGraphColors,
        dimensions: CardGraphDimensions,
    ) {
        if (chartState.selectedItemIndex != null) return

        drawMarkerIfSinglePoint(
            points = chartState.seriesPoints.speech,
            color = colors.speech,
            radius = dimensions.innerDotRadiusPx,
        )
        drawMarkerIfSinglePoint(
            points = chartState.seriesPoints.scriptMatch,
            color = colors.scriptMatch,
            radius = dimensions.innerDotRadiusPx,
        )
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

    private fun DrawScope.drawMarkerIfSinglePoint(
        points: List<Offset>,
        color: Color,
        radius: Float,
    ) {
        if (points.size != 1) return

        drawCircle(
            color = color,
            radius = radius,
            center = points.first(),
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
}

private val cardGraphTenItemPreviewItems = persistentListOf(
    CardGraphItem(
        speech = 0.76f,
        scriptMatch = 0.67f,
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
    CardGraphItem(
        speech = 0.77f,
        scriptMatch = 0.66f,
    ),
    CardGraphItem(
        speech = 0.88f,
        scriptMatch = 0.94f,
    ),
)

@Composable
private fun CardGraphPreviewContainer(
    items: ImmutableList<CardGraphItem>,
    selectedItemIndex: Int? = null,
    showDetail: Boolean = true,
    useContainerStyle: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        CardGraph(
            items = items,
            selectedItemIndex = selectedItemIndex,
            showDetail = showDetail,
            useContainerStyle = useContainerStyle,
        )
    }
}

@BasicPreview
@Composable
private fun CardGraphSingleItemPreview() {
    PrezelTheme {
        CardGraphPreviewContainer(items = cardGraphTenItemPreviewItems.take(1).toImmutableList())
    }
}

@BasicPreview
@Composable
private fun CardGraphSevenItemPreview() {
    PrezelTheme {
        CardGraphPreviewContainer(items = cardGraphTenItemPreviewItems.take(7).toImmutableList())
    }
}

@BasicPreview
@Composable
private fun CardGraphTenItemPreview() {
    PrezelTheme {
        CardGraphPreviewContainer(items = cardGraphTenItemPreviewItems)
    }
}

@BasicPreview
@Composable
private fun CardGraphInteractivePreview() {
    PrezelTheme {
        var selectedItemIndex by remember { mutableStateOf<Int?>(2) }
        var showDetail by remember { mutableStateOf(true) }
        var useContainerStyle by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                PrezelChip(
                    text = "Detail",
                    state = if (showDetail) ChipState.ACTIVE else ChipState.DEFAULT,
                    modifier = Modifier.noRippleClickable { showDetail = !showDetail },
                )
                Spacer(modifier = Modifier.width(4.dp))
                PrezelChip(
                    text = "Background",
                    state = if (useContainerStyle) ChipState.ACTIVE else ChipState.DEFAULT,
                    modifier = Modifier.noRippleClickable { useContainerStyle = !useContainerStyle },
                )
            }

            CardGraph(
                items = cardGraphTenItemPreviewItems,
                selectedItemIndex = selectedItemIndex,
                showDetail = showDetail,
                useContainerStyle = useContainerStyle,
                onSelectItem = { index ->
                    selectedItemIndex = if (selectedItemIndex == index) null else index
                },
            )
        }
    }
}
