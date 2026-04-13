package com.team.prezel.feature.history.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface HistoryUiState : UiState {
    data object Loading : HistoryUiState

    data class Content(
        val preparingPresentations: ImmutableList<HistoryUiModel>,
        val completedPresentations: ImmutableList<HistoryUiModel>,
    ) : HistoryUiState
}
