package com.team.prezel.feature.history.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.core.ui.base.UiIntent
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.history.impl.model.HistoryUiMessage

@Immutable
internal data class HistoryFailedAnalysisUiState(
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
) : UiState

internal sealed interface HistoryFailedAnalysisUiIntent : UiIntent {
    data object ClickDelete : HistoryFailedAnalysisUiIntent

    data object DismissDeleteDialog : HistoryFailedAnalysisUiIntent

    data class ConfirmDelete(
        val analysisResultId: Long?,
    ) : HistoryFailedAnalysisUiIntent
}

internal sealed interface HistoryFailedAnalysisUiEffect : UiEffect {
    data class ShowMessage(
        val message: HistoryUiMessage,
    ) : HistoryFailedAnalysisUiEffect

    data object NavigateToHistory : HistoryFailedAnalysisUiEffect
}
