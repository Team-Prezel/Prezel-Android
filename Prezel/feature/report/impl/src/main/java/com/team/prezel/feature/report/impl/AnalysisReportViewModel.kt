package com.team.prezel.feature.report.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.DeletePresentationAnalysisUseCase
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationDetailUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiEffect
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiIntent
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.contract.toAnalysisReportUiState
import com.team.prezel.feature.report.impl.model.AnalysisReportDialog
import com.team.prezel.feature.report.impl.model.AnalysisReportUiMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnalysisReportViewModel.Factory::class)
internal class AnalysisReportViewModel @AssistedInject constructor(
    @Assisted navKey: ReportNavKey,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
    private val deletePresentationAnalysisUseCase: DeletePresentationAnalysisUseCase,
) : BaseViewModel<AnalysisReportUiState, AnalysisReportUiIntent, AnalysisReportUiEffect>(AnalysisReportUiState.Loading) {
    @AssistedFactory
    interface Factory {
        fun create(navKey: ReportNavKey): AnalysisReportViewModel
    }

    private var presentationId: Long? = null
    private var analysisResultId: Long? = null

    init {
        fetchData(presentationId = navKey.presentationId, isPast = navKey.isPast)
    }

    override fun onIntent(intent: AnalysisReportUiIntent) {
        when (intent) {
            AnalysisReportUiIntent.ClickDelete -> updateContent { copy(reportDialog = AnalysisReportDialog.DELETE_REPORT) }
            is AnalysisReportUiIntent.ClickGrowthGraphItem -> updateGrowthGraphSelectedItem(index = intent.index)
            AnalysisReportUiIntent.ClickDialogConform -> handleClickDialogConform()
            AnalysisReportUiIntent.DismissDialog -> updateContent { copy(reportDialog = null) }
            AnalysisReportUiIntent.ClickReRecording -> updateContent { copy(reportDialog = AnalysisReportDialog.RE_RECORDING) }
            AnalysisReportUiIntent.ClickReWriteScript -> handleReWriteScriptClick()
            AnalysisReportUiIntent.ClickFeedbackWrite -> navigateToSelfFeedbackWrite()
        }
    }

    private fun fetchData(
        presentationId: Long,
        isPast: Boolean,
    ) {
        viewModelScope.launch {
            fetchPresentationDetailUseCase(presentationId = presentationId, isPast = isPast)
                .onSuccess { result ->
                    this@AnalysisReportViewModel.presentationId = result.presentationId
                    analysisResultId = result.analysisResultId
                    updateState { result.toAnalysisReportUiState(isPast = isPast) }
                }.onFailure {
                    sendEffect(AnalysisReportUiEffect.ShowMessage(AnalysisReportUiMessage.FETCH_REPORT_FAILED))
                    sendEffect(AnalysisReportUiEffect.NavigateToBack)
                }
        }
    }

    private fun updateGrowthGraphSelectedItem(index: Int) {
        updateContent { copy(growthGraphData = growthGraphData.copy(selectedItemIndex = index)) }
    }

    private fun handleClickDialogConform() {
        val dialog = contentState?.reportDialog ?: return

        when (dialog) {
            AnalysisReportDialog.RE_RECORDING -> navigateToAnalysisRecording()
            AnalysisReportDialog.RE_WRITE_SCRIPT -> navigateToAnalysisScript()
            AnalysisReportDialog.DELETE_REPORT -> deletePresentation()
        }
    }

    private fun deletePresentation() {
        if (analysisResultId == null) return

        viewModelScope.launch {
            deletePresentationAnalysisUseCase(analysisResultId = analysisResultId!!)
                .onSuccess { sendEffect(AnalysisReportUiEffect.NavigateToBack) }
                .onFailure { sendEffect(AnalysisReportUiEffect.ShowMessage(AnalysisReportUiMessage.DELETE_REPORT_FAILED)) }
        }
    }

    private fun navigateToAnalysisRecording() {
        val effect = presentationId?.let(AnalysisReportUiEffect::NavigateToAnalysisRecording) ?: return
        viewModelScope.launch { sendEffect(effect) }
    }

    private fun navigateToAnalysisScript() {
        val effect = presentationId?.let(AnalysisReportUiEffect::NavigateToAnalysisScript) ?: return
        viewModelScope.launch { sendEffect(effect) }
    }

    private fun handleReWriteScriptClick() {
        if (contentState?.isScriptWritten == true) return updateContent { copy(reportDialog = AnalysisReportDialog.RE_WRITE_SCRIPT) }

        navigateToAnalysisScript()
    }

    private fun navigateToSelfFeedbackWrite() {
        val effect = presentationId?.let(AnalysisReportUiEffect::NavigateToSelfFeedbackWrite) ?: return
        viewModelScope.launch { sendEffect(effect) }
    }

    private val contentState: AnalysisReportUiState.Content? get() = (currentState as? AnalysisReportUiState.Content)

    private fun updateContent(transform: AnalysisReportUiState.Content.() -> AnalysisReportUiState.Content) {
        val state = contentState ?: return
        updateState { transform(state) }
    }
}
