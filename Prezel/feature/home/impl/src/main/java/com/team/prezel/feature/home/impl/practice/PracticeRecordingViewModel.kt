package com.team.prezel.feature.home.impl.practice

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.practice.AnalyzePracticeRecordingUseCase
import com.team.prezel.core.domain.usecase.practice.FetchPracticeScriptUseCase
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisErrorType
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisUiModel
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingViewModel @Inject constructor(
    private val audioController: RecordingAudioController,
    private val fetchPracticeScriptUseCase: FetchPracticeScriptUseCase,
    private val analyzePracticeRecordingUseCase: AnalyzePracticeRecordingUseCase,
) : BaseViewModel<PracticeRecordingUiState, PracticeRecordingUiIntent, PracticeRecordingUiEffect>(PracticeRecordingUiState()) {
    init {
        collectAudioSessionState()
        collectAudioSessionEffect()
    }

    override fun onIntent(intent: PracticeRecordingUiIntent) {
        when (intent) {
            PracticeRecordingUiIntent.LoadPracticeScript -> fetchPracticeScript()
            PracticeRecordingUiIntent.RecordAudioPermissionDenied -> showMessage(PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_DENIED)
            PracticeRecordingUiIntent.RecordAudioPermissionPermanentlyDenied -> showMessage(
                PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_PERMANENTLY_DENIED,
            )

            PracticeRecordingUiIntent.StartRecording -> {
                updateState { copy(analysisStatus = PracticeRecordingAnalysisStatus.Ready) }
                audioController.startRecording()
            }

            PracticeRecordingUiIntent.StopRecording -> audioController.stopRecording()
            PracticeRecordingUiIntent.StartPlayback -> audioController.startPlayback()
            PracticeRecordingUiIntent.StopPlayback -> audioController.stopPlayback()
            PracticeRecordingUiIntent.AnalyzeClicked -> startAnalysis()
            PracticeRecordingUiIntent.RetryRecordingClicked -> resetPracticeRecording()
        }
    }

    private fun collectAudioSessionState() {
        viewModelScope.launch {
            audioController.audioSessionState.collect { audioState ->
                updateState {
                    copy(recordingState = audioState)
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
                    updateState {
                        copy(practiceScript = script.content)
                    }
                }.onFailure {
                    showMessage(PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED)
                }
        }
    }

    private fun startAnalysis() {
        if (!currentState.analyzeEnabled) return
        val filePath = currentState.recordingFilePath ?: return

        audioController.stopPlayback()
        viewModelScope.launch {
            updateState {
                copy(analysisStatus = PracticeRecordingAnalysisStatus.Loading)
            }

            delay(ANALYSIS_LOADING_DELAY_MILLIS)

            analyzePracticeRecordingUseCase(recordingFilePath = filePath)
                .onSuccess { result ->
                    updateState {
                        copy(
                            analysisStatus = PracticeRecordingAnalysisStatus.Success(
                                result = result.toUiModel(),
                            ),
                        )
                    }
                }.onFailure {
                    updateState {
                        copy(
                            analysisStatus = PracticeRecordingAnalysisStatus.Error(
                                type = PracticeRecordingAnalysisErrorType.ANALYSIS_FAILED,
                            ),
                        )
                    }
                }
        }
    }

    private fun resetPracticeRecording() {
        audioController.reset()
        updateState {
            copy(analysisStatus = PracticeRecordingAnalysisStatus.Ready)
        }
    }

    private fun showMessage(message: PracticeRecordingUiMessage) {
        viewModelScope.launch {
            sendEffect(PracticeRecordingUiEffect.ShowMessage(message))
        }
    }

    override fun onCleared() {
        audioController.release()
        super.onCleared()
    }

    private companion object {
        const val ANALYSIS_LOADING_DELAY_MILLIS = 3_000L
    }
}

private fun AudioSessionEffect.toUiMessage(): PracticeRecordingUiMessage =
    when (this) {
        AudioSessionEffect.RecordingStartFailed -> PracticeRecordingUiMessage.RECORDING_START_FAILED
        AudioSessionEffect.RecordingStopFailed -> PracticeRecordingUiMessage.RECORDING_STOP_FAILED
        AudioSessionEffect.PlaybackStartFailed -> PracticeRecordingUiMessage.PLAYBACK_START_FAILED
    }

private fun PracticeRecordingAnalysisResult.toUiModel(): PracticeRecordingAnalysisUiModel =
    PracticeRecordingAnalysisUiModel(
        pronunciationScore = pronunciationScore,
        speed = speed,
    )
