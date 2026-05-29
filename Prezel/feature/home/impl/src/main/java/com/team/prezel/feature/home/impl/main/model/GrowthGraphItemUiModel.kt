package com.team.prezel.feature.home.impl.main.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
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
    val items: List<GrowthGraphItemUiModel>,
    val selectedItemIndex: Int? = null,
) {
    val selectedItem: GrowthGraphItemUiModel? = selectedItemIndex?.let { index -> items.getOrNull(index) }
    val graphItems: ImmutableList<CardGraphItem> = items.map(GrowthGraphItemUiModel::graphItem).toImmutableList()

    companion object {
        fun List<PresentationGrowthPoint>.toUiModel(): GrowthGraphData =
            GrowthGraphData(
                items = this.map { item ->
                    GrowthGraphItemUiModel(
                        attempt = item.attempt,
                        accuracyScore = item.accuracyScore,
                        scriptMatchRate = item.scriptMatchRate,
                    )
                },
            )
    }
}

private fun Double.toGraphRatio(): Float = (this / 100.0).coerceIn(0.0, 1.0).toFloat()
