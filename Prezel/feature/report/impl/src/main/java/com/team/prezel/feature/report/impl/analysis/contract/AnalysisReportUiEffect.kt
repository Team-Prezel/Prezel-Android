package com.team.prezel.feature.report.impl.analysis.contract

import com.team.prezel.core.ui.base.UiEffect

internal sealed interface AnalysisReportUiEffect : UiEffect {
    data object NavigateHome : AnalysisReportUiEffect
}
