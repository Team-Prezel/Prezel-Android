package com.team.prezel.feature.report.impl.detail.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.component.graph.CardGraphItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class ImprovementGraphItemUiModel(
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
internal data class ImprovementGraphData(
    val items: ImmutableList<ImprovementGraphItemUiModel>,
    val selectedItemIndex: Int? = null,
) {
    val selectedItem: ImprovementGraphItemUiModel? = selectedItemIndex?.let(items::getOrNull)
    val graphItems: ImmutableList<CardGraphItem> = items.map(ImprovementGraphItemUiModel::graphItem).toImmutableList()
}

private fun Double.toGraphRatio(): Float = (this / 100.0).coerceIn(0.0, 1.0).toFloat()
