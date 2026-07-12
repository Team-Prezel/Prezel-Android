package com.team.prezel.core.ui.component.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.R
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

private const val STICK_GRAPH_MAX_HEIGHT = 160
private val STICK_GRAPH_ITEM_WIDTH = 52.dp
private val STICK_GRAPH_TRACK_HEIGHT = STICK_GRAPH_MAX_HEIGHT.dp

enum class StickGraphItemType {
    SPELLING,
    GRAMMAR,
}

@Immutable
private data class StickGraphBar(
    val count: Int,
    val height: Dp,
    val color: Color,
    val label: String,
)

@Composable
fun StickGraph(
    data: ImmutableMap<StickGraphItemType, Int>,
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
        Text(
            text = bar.count.toString(),
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textRegular,
        )

        Box(
            modifier = Modifier
                .size(
                    width = STICK_GRAPH_ITEM_WIDTH,
                    height = STICK_GRAPH_TRACK_HEIGHT,
                ).clip(PrezelTheme.shapes.V4)
                .background(color = PrezelTheme.colors.bgMedium),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(height = bar.height, width = STICK_GRAPH_ITEM_WIDTH)
                    .clip(PrezelTheme.shapes.V4)
                    .background(color = bar.color),
            )
        }

        Text(
            text = bar.label,
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textRegular,
        )
    }
}

@Composable
private fun ImmutableMap<StickGraphItemType, Int>.toStickGraphBars(): List<StickGraphBar> {
    val totalCount = values.sum()

    return StickGraphItemType.entries.map { itemType ->
        val count = getValue(itemType)

        StickGraphBar(
            count = count,
            height = count.toStickHeight(totalCount = totalCount),
            color = itemType.itemColor(),
            label = itemType.itemLabel(),
        )
    }
}

private fun Int.toStickHeight(totalCount: Int): Dp {
    if (totalCount <= 0) return 0.dp

    val heightRatio = this.toFloat() / totalCount
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
    stringResource(
        when (this) {
            StickGraphItemType.SPELLING -> R.string.core_ui_impl_stick_graph_spelling_label
            StickGraphItemType.GRAMMAR -> R.string.core_ui_impl_stick_graph_grammar_label
        },
    )

@BasicPreview
@Composable
private fun StickGraphPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier.padding(12.dp),
        ) {
            StickGraph(
                data = persistentMapOf(
                    StickGraphItemType.SPELLING to 2,
                    StickGraphItemType.GRAMMAR to 1,
                ),
            )
        }
    }
}
