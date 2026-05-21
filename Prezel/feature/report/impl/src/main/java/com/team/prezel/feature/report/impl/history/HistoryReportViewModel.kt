package com.team.prezel.feature.report.impl.history

import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiEffect
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiIntent
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = HistoryReportViewModel.Factory::class)
internal class HistoryReportViewModel @AssistedInject constructor(
    @Assisted navKey: ReportNavKey.History,
) : BaseViewModel<HistoryReportUiState, HistoryReportUiIntent, HistoryReportUiEffect>(HistoryReportUiState.Loading) {
    @AssistedFactory
    interface Factory {
        fun create(navKey: ReportNavKey.History): HistoryReportViewModel
    }

    override fun onIntent(intent: HistoryReportUiIntent) =
        when (intent) {
            HistoryReportUiIntent.ClickDelete -> Unit
        }
}
