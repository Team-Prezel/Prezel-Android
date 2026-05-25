package com.team.prezel.feature.analysis.impl.contract

import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage

internal sealed interface AnalysisFlowUiEffect : UiEffect {
    data object NavigateBack : AnalysisFlowUiEffect

    data class NavigateToReport(
        val result: PresentationAnalysisSummary,
    ) : AnalysisFlowUiEffect

    data class ShowMessage(
        val message: AnalysisUiMessage,
    ) : AnalysisFlowUiEffect
}
