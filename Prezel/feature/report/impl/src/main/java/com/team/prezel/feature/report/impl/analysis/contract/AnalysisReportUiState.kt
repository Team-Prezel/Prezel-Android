package com.team.prezel.feature.report.impl.analysis.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel

@Immutable
internal sealed interface AnalysisReportUiState : UiState {
    data object Loading : AnalysisReportUiState

    data class Content(
        val reportDetail: ReportDetailUiModel,
    ) : AnalysisReportUiState
}
