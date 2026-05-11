package com.team.prezel.core.ui.component.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val STICK_GRAPH_MAX_HEIGHT = 160
private val STICK_GRAPH_ITEM_WIDTH = 52.dp

enum class StickGraphItemType {
    SPELLING,
    GRAMMAR,
}

@Immutable
data class StickData(
    val count: Int,
    val itemType: StickGraphItemType,
)

@Immutable
private data class StickGraphBar(
    val count: Int,
    val height: Dp,
    val color: Color,
    val label: String,
)

@Composable
fun StickGraph(
    data: ImmutableList<StickData>,
    modifier: Modifier = Modifier,
) {
    require(data.size == StickGraphItemType.entries.size) {
        "StickGraph 데이터는 각 항목별로 1개씩, 총 ${StickGraphItemType.entries.size}개가 필요합니다."
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        verticalAlignment = Alignment.Bottom,
    ) {
        data.toStickGraphBars().forEach { bar ->
            StickGraphItem(bar = bar)
        }
    }
}

@Composable
private fun StickGraphItem(
    bar: StickGraphBar,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        ProvideTextStyle(PrezelTheme.typography.body3Medium.copy(color = PrezelTheme.colors.textRegular)) {
            Text(text = bar.count.toString())

            Box(
                modifier = modifier
                    .size(
                        width = STICK_GRAPH_ITEM_WIDTH,
                        height = bar.height,
                    ).clip(PrezelTheme.shapes.V4)
                    .background(color = bar.color),
            )

            Text(text = bar.label)
        }
    }
}

@Composable
private fun ImmutableList<StickData>.toStickGraphBars(): ImmutableList<StickGraphBar> {
    val aggregatedData = aggregateByItemType()
    val maxCount = aggregatedData.maxOf(StickData::count)

    return aggregatedData
        .map { data ->
            StickGraphBar(
                count = data.count,
                height = data.count.toStickHeight(maxCount = maxCount),
                color = data.itemType.itemColor(),
                label = data.itemType.itemLabel(),
            )
        }.toImmutableList()
}

private fun ImmutableList<StickData>.aggregateByItemType(): ImmutableList<StickData> =
    this
        .groupBy { stickItem -> stickItem.itemType }
        .map { (itemType, items) ->
            StickData(
                count = items.sumOf { item -> item.count },
                itemType = itemType,
            )
        }.toImmutableList()

private fun Int.toStickHeight(maxCount: Int): Dp {
    val heightRatio = this.toFloat() / maxCount
    return (heightRatio * STICK_GRAPH_MAX_HEIGHT).dp
}

@Composable
private fun StickGraphItemType.itemColor(): Color =
    when (this) {
        StickGraphItemType.SPELLING -> PrezelTheme.colors.accentPurpleRegular
        StickGraphItemType.GRAMMAR -> PrezelTheme.colors.accentTealRegular
    }

@Composable
private fun StickGraphItemType.itemLabel(): String =
    when (this) {
        StickGraphItemType.SPELLING -> R.string.core_ui_impl_stick_graph_spelling_label
        StickGraphItemType.GRAMMAR -> R.string.core_ui_impl_stick_graph_grammar_label
    }.let { resId -> stringResource(resId) }

@Preview(showBackground = true)
@Composable
private fun StickGraphPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier.padding(12.dp),
        ) {
            StickGraph(
                data = listOf(
                    StickData(count = 2, itemType = StickGraphItemType.SPELLING),
                    StickData(count = 1, itemType = StickGraphItemType.GRAMMAR),
                ).toImmutableList(),
            )
        }
    }
}
