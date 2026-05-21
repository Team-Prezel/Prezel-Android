package com.team.prezel.feature.report.impl.history.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.report.impl.detail.model.PracticeUiModel
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface HistoryReportUiState : UiState {
    data object Loading : HistoryReportUiState

    data class Content(
        val reportDetail: ReportDetailUiModel,
        val selfFeedback: String?,
        val practices: ImmutableList<PracticeUiModel>,
    ) : HistoryReportUiState
}
