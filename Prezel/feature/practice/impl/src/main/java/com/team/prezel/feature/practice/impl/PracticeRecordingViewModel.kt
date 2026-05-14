package com.team.prezel.feature.practice.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.usecase.practice.AnalyzePracticeRecordingUseCase
import com.team.prezel.core.domain.usecase.practice.FetchPracticeScriptUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiState
import com.team.prezel.feature.practice.impl.model.PracticeRecordingAnalysisErrorType
import com.team.prezel.feature.practice.impl.model.PracticeRecordingUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingViewModel @Inject constructor(
    private val audioController: RecordingAudioController,
    private val fetchPracticeScriptUseCase: FetchPracticeScriptUseCase,
    private val analyzePracticeRecordingUseCase: AnalyzePracticeRecordingUseCase,
) : BaseViewModel<PracticeRecordingUiState, PracticeRecordingUiIntent, PracticeRecordingUiEffect>(PracticeRecordingUiState.Ready()) {
    private var practiceScript: String = ""

    init {
        collectAudioSessionState()
        collectAudioSessionEffect()
        fetchPracticeScript()
    }

    override fun onIntent(intent: PracticeRecordingUiIntent) {
        when (intent) {
            PracticeRecordingUiIntent.StartRecording -> {
                if (practiceScript.isBlank()) {
                    showMessage(PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED)
                    return
                }
                audioController.startRecording()
            }

            PracticeRecordingUiIntent.StopRecording -> audioController.stopRecording()
            PracticeRecordingUiIntent.StartPlayback -> audioController.startPlayback()
            PracticeRecordingUiIntent.StopPlayback -> audioController.stopPlayback()
            PracticeRecordingUiIntent.AnalyzeRecording -> startAnalysis()
            PracticeRecordingUiIntent.ResetRecording -> resetPracticeRecording()
        }
    }

    private fun collectAudioSessionState() {
        viewModelScope.launch {
            audioController.audioSessionState.collect { audioState ->
                updateState {
                    when {
                        audioState is AudioSessionState.Recording -> PracticeRecordingUiState.Ready(
                            practiceScript = this@PracticeRecordingViewModel.practiceScript,
                            recordingState = audioState,
                        )

                        this is PracticeRecordingUiState.Ready -> copy(recordingState = audioState)
                        else -> this
                    }
                }
            }
        }
    }

    private fun collectAudioSessionEffect() {
        viewModelScope.launch {
            audioController.audioSessionEffect.collect { effect ->
                showMessage(effect.toUiMessage())
            }
        }
    }

    private fun fetchPracticeScript() {
        viewModelScope.launch {
            fetchPracticeScriptUseCase()
                .onSuccess { script ->
                    practiceScript = script.content
                    updateState {
                        (this as PracticeRecordingUiState.Ready).copy(practiceScript = script.content)
                    }
                }.onFailure {
                    showMessage(PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED)
                }
        }
    }

    private fun startAnalysis() {
        val readyState = currentState as? PracticeRecordingUiState.Ready ?: return
        if (!readyState.analyzeEnabled) return
        val filePath = readyState.recordingFilePath ?: return
        val referenceText = practiceScript

        audioController.stopPlayback()
        viewModelScope.launch {
            updateState {
                PracticeRecordingUiState.Analysis.Loading
            }

            analyzePracticeRecordingUseCase(
                recordingFilePath = filePath,
                referenceText = referenceText,
            ).onSuccess { result ->
                updateState {
                    PracticeRecordingUiState.Analysis.Success(
                        result = result,
                    )
                }
            }.onFailure { throwable ->
                updateState {
                    PracticeRecordingUiState.Analysis.Error(
                        type = throwable.toPracticeRecordingAnalysisErrorType(),
                    )
                }
            }
        }
    }

    private fun resetPracticeRecording() {
        audioController.reset()
        updateState {
            PracticeRecordingUiState.Ready(practiceScript = practiceScript)
        }
    }

    private fun showMessage(message: PracticeRecordingUiMessage) {
        viewModelScope.launch {
            sendEffect(PracticeRecordingUiEffect.ShowMessage(message))
        }
    }

    private fun AudioSessionEffect.toUiMessage(): PracticeRecordingUiMessage =
        when (this) {
            AudioSessionEffect.RecordingStartFailed -> PracticeRecordingUiMessage.RECORDING_START_FAILED
            AudioSessionEffect.RecordingStopFailed -> PracticeRecordingUiMessage.RECORDING_STOP_FAILED
            AudioSessionEffect.PlaybackStartFailed -> PracticeRecordingUiMessage.PLAYBACK_START_FAILED
        }

    private fun Throwable.toPracticeRecordingAnalysisErrorType(): PracticeRecordingAnalysisErrorType {
        val error = (this as? AppException)?.error

        return when (error) {
            AppError.VOICE_RECOGNITION_FAILED -> PracticeRecordingAnalysisErrorType.VOICE_RECOGNITION_FAILED
            else -> PracticeRecordingAnalysisErrorType.ANALYSIS_FAILED
        }
    }

    override fun onCleared() {
        audioController.release()
        super.onCleared()
    }
}
