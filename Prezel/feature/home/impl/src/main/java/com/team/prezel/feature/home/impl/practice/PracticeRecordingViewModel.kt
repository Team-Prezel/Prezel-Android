package com.team.prezel.feature.home.impl.practice

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEvent
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.practice.AnalyzePracticeRecordingUseCase
import com.team.prezel.core.domain.usecase.practice.FetchPracticeScriptUseCase
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisErrorType
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisSpeed
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisUiModel
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingState
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
        collectAudioSessionEvent()
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

            PracticeRecordingUiIntent.PauseRecording -> audioController.pauseRecording()
            PracticeRecordingUiIntent.ResumeRecording -> audioController.resumeRecording()
            PracticeRecordingUiIntent.StopRecording -> audioController.stopRecording()
            PracticeRecordingUiIntent.ResetRecording -> {
                updateState { copy(analysisStatus = PracticeRecordingAnalysisStatus.Ready) }
                audioController.resetRecording()
            }

            is PracticeRecordingUiIntent.AudioFileSelected -> {
                updateState { copy(analysisStatus = PracticeRecordingAnalysisStatus.Ready) }
                audioController.loadAudioFile(intent.uri)
            }

            PracticeRecordingUiIntent.StartPlayback -> audioController.startPlayback()
            PracticeRecordingUiIntent.PausePlayback -> audioController.pausePlayback()
            PracticeRecordingUiIntent.ResumePlayback -> audioController.resumePlayback()
            PracticeRecordingUiIntent.StopPlayback -> audioController.stopPlayback()
            PracticeRecordingUiIntent.AnalyzeClicked -> startAnalysis()
        }
    }

    private fun collectAudioSessionState() {
        viewModelScope.launch {
            audioController.audioSessionState.collect { audioState ->
                updateState {
                    copy(recordingState = audioState.toPracticeRecordingState())
                }
            }
        }
    }

    private fun collectAudioSessionEvent() {
        viewModelScope.launch {
            audioController.audioSessionEvent.collect { event ->
                showMessage(event.toUiMessage())
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
        val filePath = currentState.recordingState.filePath ?: return

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

private fun AudioSessionState.toPracticeRecordingState(): PracticeRecordingState =
    when (this) {
        AudioSessionState.Idle -> PracticeRecordingState.Idle
        is AudioSessionState.Recording -> PracticeRecordingState.Recording(
            recordingSeconds = elapsedSeconds,
        )

        is AudioSessionState.RecordingPaused -> PracticeRecordingState.RecordingPaused(
            recordingSeconds = elapsedSeconds,
        )

        is AudioSessionState.ReadyToPlay -> PracticeRecordingState.ReadyToPlay(
            filePath = source.filePath,
            durationSeconds = durationSeconds,
            sourceType = source.toPracticeSourceType(),
        )

        is AudioSessionState.Playing -> PracticeRecordingState.Playing(
            filePath = source.filePath,
            playbackSeconds = positionSeconds,
            durationSeconds = durationSeconds,
            sourceType = source.toPracticeSourceType(),
        )

        is AudioSessionState.PlaybackPaused -> PracticeRecordingState.PlaybackPaused(
            filePath = source.filePath,
            playbackSeconds = positionSeconds,
            durationSeconds = durationSeconds,
            sourceType = source.toPracticeSourceType(),
        )
    }

private fun AudioSource.toPracticeSourceType(): PracticeRecordingState.SourceType =
    when (this) {
        is AudioSource.RecordedFile -> PracticeRecordingState.SourceType.RECORDED_FILE
        is AudioSource.ExternalFile -> PracticeRecordingState.SourceType.EXTERNAL_FILE
    }

private fun AudioSessionEvent.toUiMessage(): PracticeRecordingUiMessage =
    when (this) {
        AudioSessionEvent.RecordingStartFailed,
        AudioSessionEvent.RecordingPauseFailed,
        AudioSessionEvent.RecordingResumeFailed,
        -> PracticeRecordingUiMessage.RECORDING_START_FAILED

        AudioSessionEvent.RecordingStopFailed -> PracticeRecordingUiMessage.RECORDING_STOP_FAILED
        AudioSessionEvent.PlaybackStartFailed -> PracticeRecordingUiMessage.PLAYBACK_START_FAILED
        AudioSessionEvent.FileLoadFailed -> PracticeRecordingUiMessage.AUDIO_FILE_LOAD_FAILED
    }

private fun PracticeRecordingAnalysisResult.toUiModel(): PracticeRecordingAnalysisUiModel =
    PracticeRecordingAnalysisUiModel(
        pronunciationScore = pronunciationScore,
        speed = speed.toUiModel(),
    )

private fun PracticeRecordingSpeed.toUiModel(): PracticeRecordingAnalysisSpeed =
    when (this) {
        PracticeRecordingSpeed.SLOW -> PracticeRecordingAnalysisSpeed.SLOW
        PracticeRecordingSpeed.ADEQUATE -> PracticeRecordingAnalysisSpeed.ADEQUATE
        PracticeRecordingSpeed.FAST -> PracticeRecordingAnalysisSpeed.FAST
    }
