package com.team.prezel.feature.report.impl.analysis.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface AnalysisReportUiIntent : UiIntent {
    data object ClickSave : AnalysisReportUiIntent
}
