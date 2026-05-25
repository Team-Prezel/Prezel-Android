package com.team.prezel.feature.report.impl.contract

import com.team.prezel.core.ui.base.UiEffect

internal sealed interface AnalysisReportUiEffect : UiEffect {
    data object NavigateToBack : AnalysisReportUiEffect

    data class NavigateToAnalysisScript(
        val presentationId: Long,
    ) : AnalysisReportUiEffect

    data class NavigateToAnalysisRecording(
        val presentationId: Long,
    ) : AnalysisReportUiEffect
}
