package com.team.prezel.feature.practice.impl.analysis

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.usecase.practice.AnalyzePracticeRecordingUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeAnalysisUiEffect
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeAnalysisUiIntent
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeAnalysisUiState
import com.team.prezel.feature.practice.impl.analysis.model.PracticeAnalysisErrorType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PracticeAnalysisViewModel.Factory::class)
internal class PracticeAnalysisViewModel @AssistedInject constructor(
    @Assisted("presentationId") private val presentationId: Long,
    @Assisted("recordingFilePath") private val recordingFilePath: String,
    @Assisted("referenceText") private val referenceText: String,
    private val analyzePracticeRecordingUseCase: AnalyzePracticeRecordingUseCase,
) : BaseViewModel<PracticeAnalysisUiState, PracticeAnalysisUiIntent, PracticeAnalysisUiEffect>(
        PracticeAnalysisUiState.Loading,
    ) {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("presentationId") presentationId: Long,
            @Assisted("recordingFilePath") recordingFilePath: String,
            @Assisted("referenceText") referenceText: String,
        ): PracticeAnalysisViewModel
    }

    init {
        analyzeRecording()
    }

    override fun onIntent(intent: PracticeAnalysisUiIntent) = Unit

    private fun analyzeRecording() {
        viewModelScope.launch {
            updateState { PracticeAnalysisUiState.Loading }

            analyzePracticeRecordingUseCase(
                presentationId = presentationId,
                recordingFilePath = recordingFilePath,
                referenceText = referenceText,
            ).onSuccess { result ->
                updateState {
                    PracticeAnalysisUiState.Success(
                        pronunciationScore = result.pronunciationScore,
                        speed = result.speed,
                        overallEvaluation = result.overallEvaluation,
                    )
                }
            }.onFailure { throwable ->
                updateState {
                    PracticeAnalysisUiState.Error(
                        type = throwable.toPracticeRecordingAnalysisErrorType(),
                    )
                }
            }
        }
    }

    private fun Throwable.toPracticeRecordingAnalysisErrorType(): PracticeAnalysisErrorType {
        val error = (this as? AppException)?.error

        return when (error) {
            AppError.VOICE_RECOGNITION_FAILED -> PracticeAnalysisErrorType.VOICE_RECOGNITION_FAILED
            else -> PracticeAnalysisErrorType.ANALYSIS_FAILED
        }
    }
}
