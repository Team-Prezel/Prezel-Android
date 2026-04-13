package com.team.prezel.feature.history.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState

@Immutable
internal sealed interface HistoryUiState : UiState {
    data object Loading : HistoryUiState
}
