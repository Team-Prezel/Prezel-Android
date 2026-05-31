package com.team.prezel.feature.report.impl.report.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.report.impl.report.model.AnalysisReportUiMessage

internal sealed interface AnalysisReportUiEffect : UiEffect {
    data object NavigateToBack : AnalysisReportUiEffect

    data class ShowMessage(
        val message: AnalysisReportUiMessage,
    ) : AnalysisReportUiEffect

    data class NavigateToAnalysisScript(
        val presentationId: Long,
        val isPast: Boolean,
    ) : AnalysisReportUiEffect

    data class NavigateToAnalysisRecording(
        val presentationId: Long,
        val isPast: Boolean,
    ) : AnalysisReportUiEffect

    data class NavigateToSelfFeedbackWrite(
        val presentationId: Long,
    ) : AnalysisReportUiEffect

    data class NavigateToScriptAnalysis(
        val analysisResultId: Long,
    ) : AnalysisReportUiEffect
}
