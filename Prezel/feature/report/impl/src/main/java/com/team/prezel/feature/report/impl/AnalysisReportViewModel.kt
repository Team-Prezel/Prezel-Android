package com.team.prezel.feature.report.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationDetailUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiEffect
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiIntent
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.contract.toAnalysisReportUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnalysisReportViewModel.Factory::class)
internal class AnalysisReportViewModel @AssistedInject constructor(
    @Assisted navKey: ReportNavKey,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
) : BaseViewModel<AnalysisReportUiState, AnalysisReportUiIntent, AnalysisReportUiEffect>(AnalysisReportUiState.Loading) {
    @AssistedFactory
    interface Factory {
        fun create(navKey: ReportNavKey): AnalysisReportViewModel
    }

    init {
        fetchData(presentationId = navKey.presentationId, isPast = navKey.isPast)
    }

    override fun onIntent(intent: AnalysisReportUiIntent) {
        when (intent) {
            AnalysisReportUiIntent.ClickDelete -> deletePresentation()
            is AnalysisReportUiIntent.ClickGrowthGraphItem -> updateGrowthGraphSelectedItem(index = intent.index)
        }
    }

    private fun fetchData(
        presentationId: Long,
        isPast: Boolean,
    ) {
        viewModelScope.launch {
            fetchPresentationDetailUseCase(presentationId = presentationId, isPast = isPast)
                .onSuccess { result -> updateState { result.toAnalysisReportUiState(isPast = isPast) } }
                .onFailure { }
        }
    }

    private fun updateGrowthGraphSelectedItem(index: Int) {
        val state = (currentState as? AnalysisReportUiState.Content) ?: return
        updateState { state.copy(growthGraphData = state.growthGraphData.copy(selectedItemIndex = index)) }
    }

    private fun deletePresentation() {
        viewModelScope.launch {
            sendEffect(AnalysisReportUiEffect.NavigateHome)
        }
    }
}
