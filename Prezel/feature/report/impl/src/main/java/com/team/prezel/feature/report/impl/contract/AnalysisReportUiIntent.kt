package com.team.prezel.feature.report.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface AnalysisReportUiIntent : UiIntent {
    data object ClickDelete : AnalysisReportUiIntent

    data class ClickGrowthGraphItem(
        val index: Int,
    ) : AnalysisReportUiIntent

    data object DismissDialog : AnalysisReportUiIntent

    data object ClickDialogConform : AnalysisReportUiIntent
}
