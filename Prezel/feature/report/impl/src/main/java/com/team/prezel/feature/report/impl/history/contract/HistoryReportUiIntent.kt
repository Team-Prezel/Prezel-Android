package com.team.prezel.feature.report.impl.history.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface HistoryReportUiIntent : UiIntent {
    data object ClickDelete : HistoryReportUiIntent
}
