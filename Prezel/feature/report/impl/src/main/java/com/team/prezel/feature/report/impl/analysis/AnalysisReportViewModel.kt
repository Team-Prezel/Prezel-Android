package com.team.prezel.feature.report.impl.analysis

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiEffect
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiIntent
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.analysis.contract.toAnalysisReportUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnalysisReportViewModel.Factory::class)
internal class AnalysisReportViewModel @AssistedInject constructor(
    @Assisted navKey: ReportNavKey.Analysis,
) : BaseViewModel<AnalysisReportUiState, AnalysisReportUiIntent, AnalysisReportUiEffect>(navKey.payload.toAnalysisReportUiState()) {
    @AssistedFactory
    interface Factory {
        fun create(navKey: ReportNavKey.Analysis): AnalysisReportViewModel
    }

    override fun onIntent(intent: AnalysisReportUiIntent) {
        when (intent) {
            AnalysisReportUiIntent.ClickSave -> viewModelScope.launch { sendEffect(AnalysisReportUiEffect.NavigateHome) }
        }
    }
}
