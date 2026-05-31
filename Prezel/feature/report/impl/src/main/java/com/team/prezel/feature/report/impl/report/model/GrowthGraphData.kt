package com.team.prezel.feature.report.impl.report.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.component.graph.CardGraphItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class GrowthGraphItemUiModel(
    val attempt: Int,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
) {
    val graphItem: CardGraphItem
        get() = CardGraphItem(
            speech = accuracyScore.toGraphRatio(),
            scriptMatch = scriptMatchRate.toGraphRatio(),
        )
}

@Immutable
internal data class GrowthGraphData(
    val items: ImmutableList<GrowthGraphItemUiModel>,
    val selectedItemIndex: Int = (items.size / 2).coerceAtMost(3),
) {
    init {
        require(selectedItemIndex in 0..items.size) { "$selectedItemIndex" }
    }

    val selectedItem: GrowthGraphItemUiModel? = items.getOrNull(selectedItemIndex)
    val graphItems: ImmutableList<CardGraphItem> = items.map(GrowthGraphItemUiModel::graphItem).toImmutableList()
}

private fun Double.toGraphRatio(): Float = (this / 100.0).coerceIn(0.0, 1.0).toFloat()
