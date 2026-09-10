package com.team.prezel.feature.history.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.DeletePresentationAnalysisUseCase
import com.team.prezel.core.domain.usecase.presentation.ReAnalyzePresentationV2UseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiState
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HistoryFailedAnalysisViewModel @Inject constructor(
    private val reAnalyzePresentationV2UseCase: ReAnalyzePresentationV2UseCase,
    private val deletePresentationAnalysisUseCase: DeletePresentationAnalysisUseCase,
) : BaseViewModel<HistoryFailedAnalysisUiState, HistoryFailedAnalysisUiIntent, HistoryFailedAnalysisUiEffect>(
        HistoryFailedAnalysisUiState(),
    ) {
    override fun onIntent(intent: HistoryFailedAnalysisUiIntent) {
        when (intent) {
            HistoryFailedAnalysisUiIntent.ClickDelete -> updateState { copy(showDeleteDialog = true) }
            HistoryFailedAnalysisUiIntent.DismissDeleteDialog -> updateState { copy(showDeleteDialog = false) }
            is HistoryFailedAnalysisUiIntent.ConfirmDelete -> deleteAnalysis(analysisResultId = intent.analysisResultId)
            is HistoryFailedAnalysisUiIntent.Retry -> retryAnalysis(
                presentationId = intent.presentationId,
                script = intent.script,
                audioUrl = intent.audioUrl,
            )
        }
    }

    private fun retryAnalysis(
        presentationId: Long,
        script: String?,
        audioUrl: String?,
    ) {
        if (audioUrl.isNullOrBlank()) {
            viewModelScope.launch {
                sendEffect(HistoryFailedAnalysisUiEffect.ShowMessage(HistoryUiMessage.RETRY_DATA_MISSING))
            }
            return
        }

        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            reAnalyzePresentationV2UseCase(
                presentationId = presentationId,
                script = script,
                scriptFilePath = null,
                audioFilePath = null,
                audioUrl = audioUrl,
            ).fold(
                onSuccess = {
                    updateState { copy(isLoading = false) }
                    sendEffect(HistoryFailedAnalysisUiEffect.NavigateToHistory)
                },
                onFailure = {
                    updateState { copy(isLoading = false) }
                    sendEffect(HistoryFailedAnalysisUiEffect.ShowMessage(HistoryUiMessage.RETRY_FAILED))
                },
            )
        }
    }

    private fun deleteAnalysis(analysisResultId: Long?) {
        if (analysisResultId == null) {
            viewModelScope.launch {
                sendEffect(HistoryFailedAnalysisUiEffect.ShowMessage(HistoryUiMessage.DELETE_DATA_MISSING))
            }
            return
        }

        updateState { copy(isLoading = true, showDeleteDialog = false) }
        viewModelScope.launch {
            deletePresentationAnalysisUseCase(analysisResultId = analysisResultId).fold(
                onSuccess = {
                    updateState { copy(isLoading = false) }
                    sendEffect(HistoryFailedAnalysisUiEffect.NavigateToHistory)
                },
                onFailure = {
                    updateState { copy(isLoading = false) }
                    sendEffect(HistoryFailedAnalysisUiEffect.ShowMessage(HistoryUiMessage.DELETE_FAILED))
                },
            )
        }
    }
}
