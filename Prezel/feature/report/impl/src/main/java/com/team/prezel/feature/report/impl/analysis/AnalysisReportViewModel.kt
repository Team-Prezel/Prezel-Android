package com.team.prezel.feature.report.impl.analysis

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationDetailUseCase
import com.team.prezel.core.ui.base.BaseViewModel
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
    @Assisted presentationId: Long,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
) : BaseViewModel<AnalysisReportUiState, AnalysisReportUiIntent, AnalysisReportUiEffect>(AnalysisReportUiState.Loading) {
    @AssistedFactory
    interface Factory {
        fun create(presentationId: Long): AnalysisReportViewModel
    }

    init {
        fetchData(presentationId = presentationId)
    }

    override fun onIntent(intent: AnalysisReportUiIntent) {
        when (intent) {
            AnalysisReportUiIntent.ClickDelete -> viewModelScope.launch { sendEffect(AnalysisReportUiEffect.NavigateHome) }
        }
    }

    private fun fetchData(presentationId: Long) {
        viewModelScope.launch {
            fetchPresentationDetailUseCase(presentationId = presentationId)
                .onSuccess { result -> updateState { result.toAnalysisReportUiState() } }
                .onFailure { }
        }
    }
}
