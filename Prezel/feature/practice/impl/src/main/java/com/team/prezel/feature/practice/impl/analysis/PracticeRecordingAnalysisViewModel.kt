package com.team.prezel.feature.practice.impl.analysis

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.usecase.practice.AnalyzePracticeRecordingUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeRecordingAnalysisUiEffect
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeRecordingAnalysisUiIntent
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeRecordingAnalysisUiState
import com.team.prezel.feature.practice.impl.model.PracticeRecordingAnalysisErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingAnalysisViewModel @Inject constructor(
    private val analyzePracticeRecordingUseCase: AnalyzePracticeRecordingUseCase,
) : BaseViewModel<PracticeRecordingAnalysisUiState, PracticeRecordingAnalysisUiIntent, PracticeRecordingAnalysisUiEffect>(
        PracticeRecordingAnalysisUiState.Loading,
    ) {
    private var analysisRequest: AnalysisRequest? = null

    override fun onIntent(intent: PracticeRecordingAnalysisUiIntent) {
        when (intent) {
            is PracticeRecordingAnalysisUiIntent.Analyze -> analyzeRecording(
                recordingFilePath = intent.recordingFilePath,
                referenceText = intent.referenceText,
            )
        }
    }

    private fun analyzeRecording(
        recordingFilePath: String,
        referenceText: String,
    ) {
        val request = AnalysisRequest(
            recordingFilePath = recordingFilePath,
            referenceText = referenceText,
        )
        if (analysisRequest == request) return

        analysisRequest = request
        viewModelScope.launch {
            updateState { PracticeRecordingAnalysisUiState.Loading }

            analyzePracticeRecordingUseCase(
                recordingFilePath = recordingFilePath,
                referenceText = referenceText,
            ).onSuccess { result ->
                updateState {
                    PracticeRecordingAnalysisUiState.Success(result = result)
                }
            }.onFailure { throwable ->
                updateState {
                    PracticeRecordingAnalysisUiState.Error(
                        type = throwable.toPracticeRecordingAnalysisErrorType(),
                    )
                }
            }
        }
    }

    private data class AnalysisRequest(
        val recordingFilePath: String,
        val referenceText: String,
    )

    private fun Throwable.toPracticeRecordingAnalysisErrorType(): PracticeRecordingAnalysisErrorType {
        val error = (this as? AppException)?.error

        return when (error) {
            AppError.VOICE_RECOGNITION_FAILED -> PracticeRecordingAnalysisErrorType.VOICE_RECOGNITION_FAILED
            else -> PracticeRecordingAnalysisErrorType.ANALYSIS_FAILED
        }
    }
}
