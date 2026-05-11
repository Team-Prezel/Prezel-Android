package com.team.prezel.feature.report.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

@Immutable
internal sealed interface ReportUiState : UiState {
    data object Loading : ReportUiState

    data object Content : ReportUiState
}
