package com.team.prezel.feature.history.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.history.impl.model.HistoryPageType
import com.team.prezel.feature.history.impl.model.HistoryPageUiModel
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface HistoryUiState : UiState {
    data object Loading : HistoryUiState

    data class Content(
        val pages: ImmutableList<HistoryPageUiModel>,
    ) : HistoryUiState {
        fun currentPageItem(type: HistoryPageType): List<HistoryUiModel> = pages.firstOrNull { page -> page.type == type }?.items.orEmpty()
    }
}
