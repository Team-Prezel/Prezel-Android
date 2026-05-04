package com.team.prezel.feature.home.impl.practice

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.practice.AnalyzePracticeRecordingUseCase
import com.team.prezel.core.domain.usecase.practice.FetchPracticeScriptUseCase
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.practice.audio.RecordingAudioController
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingViewModel @Inject constructor(
    private val audioController: RecordingAudioController,
    private val fetchPracticeScriptUseCase: FetchPracticeScriptUseCase,
    private val analyzePracticeRecordingUseCase: AnalyzePracticeRecordingUseCase,
) : BaseViewModel<PracticeRecordingUiState, PracticeRecordingUiIntent, PracticeRecordingUiEffect>(PracticeRecordingUiState()) {
    private var recordingFilePath: String? = null
    private var timerJob: Job? = null
    private var analysisJob: Job? = null

    override fun onIntent(intent: PracticeRecordingUiIntent) {
        when (intent) {
            PracticeRecordingUiIntent.LoadPracticeScript -> fetchPracticeScript()
            PracticeRecordingUiIntent.RecordAudioPermissionDenied -> showMessage(PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_DENIED)
            PracticeRecordingUiIntent.RecordAudioPermissionPermanentlyDenied -> showMessage(
                PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_PERMANENTLY_DENIED,
            )

            PracticeRecordingUiIntent.ToggleRecordingControl -> toggleRecordingControl()
            PracticeRecordingUiIntent.AnalyzeClicked -> startAnalysis()
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

    private fun toggleRecordingControl() {
        when (currentState.recordingState) {
            PracticeRecordingState.Idle -> startRecording()
            is PracticeRecordingState.Recording -> stopRecording()
            is PracticeRecordingState.Recorded -> startPlayback()
            is PracticeRecordingState.Playing -> stopPlayback()
        }
    }

    private fun startRecording() {
        audioController
            .startRecording()
            .onSuccess { filePath ->
                recordingFilePath = filePath

                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Recording(
                            recordingSeconds = 0,
                        ),
                        analysisStatus = PracticeRecordingAnalysisStatus.Ready,
                    )
                }

                startRecordingTimer()
            }.onFailure {
                recordingFilePath = null
                timerJob?.cancel()
                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Idle,
                        analysisStatus = PracticeRecordingAnalysisStatus.Ready,
                    )
                }
                showMessage(PracticeRecordingUiMessage.RECORDING_START_FAILED)
            }
    }

    private fun stopRecording() {
        val previousState = currentState.recordingState
        if (previousState !is PracticeRecordingState.Recording) return

        timerJob?.cancel()

        audioController
            .stopRecording()
            .onSuccess { durationSeconds ->
                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Recorded(
                            recordedDurationSeconds = durationSeconds.coerceAtLeast(
                                previousState.recordingSeconds,
                            ),
                        ),
                    )
                }
            }.onFailure {
                recordingFilePath = null
                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Idle,
                        analysisStatus = PracticeRecordingAnalysisStatus.Ready,
                    )
                }
                showMessage(PracticeRecordingUiMessage.RECORDING_STOP_FAILED)
            }
    }

    private fun startPlayback() {
        val previousState = currentState.recordingState
        if (previousState !is PracticeRecordingState.Recorded) return

        val filePath = recordingFilePath
        if (filePath == null) {
            showMessage(PracticeRecordingUiMessage.PLAYBACK_START_FAILED)
            return
        }

        audioController
            .startPlayback(filePath) {
                timerJob?.cancel()
                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Recorded(
                            recordedDurationSeconds = previousState.recordedDurationSeconds,
                        ),
                    )
                }
            }.onSuccess { durationSeconds ->
                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Playing(
                            playbackSeconds = 0,
                            recordedDurationSeconds = durationSeconds.coerceAtLeast(
                                previousState.recordedDurationSeconds,
                            ),
                        ),
                    )
                }

                startPlaybackTimer()
            }.onFailure {
                updateState {
                    copy(
                        recordingState = PracticeRecordingState.Recorded(
                            recordedDurationSeconds = previousState.recordedDurationSeconds,
                        ),
                        analysisStatus = PracticeRecordingAnalysisStatus.Ready,
                    )
                }
                showMessage(PracticeRecordingUiMessage.PLAYBACK_START_FAILED)
            }
    }

    private fun stopPlayback() {
        val previousState = currentState.recordingState
        if (previousState !is PracticeRecordingState.Playing) return

        audioController.stopPlayback()
        timerJob?.cancel()

        updateState {
            copy(
                recordingState = PracticeRecordingState.Recorded(
                    recordedDurationSeconds = previousState.recordedDurationSeconds,
                ),
            )
        }
    }

    private fun startAnalysis() {
        if (!currentState.analyzeEnabled) return
        val filePath = recordingFilePath ?: return

        audioController.stopPlayback()
        analysisJob?.cancel()
        analysisJob = viewModelScope.launch {
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

    private fun startRecordingTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(TIMER_DELAY_MILLIS)

                updateState {
                    val state = recordingState
                    if (state !is PracticeRecordingState.Recording) return@updateState this

                    copy(
                        recordingState = PracticeRecordingState.Recording(
                            recordingSeconds = state.recordingSeconds + 1,
                        ),
                    )
                }
            }
        }
    }

    private fun startPlaybackTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(PLAYBACK_TIMER_DELAY_MILLIS)

                updateState {
                    val state = recordingState
                    if (state !is PracticeRecordingState.Playing) return@updateState this

                    copy(
                        recordingState = PracticeRecordingState.Playing(
                            playbackSeconds = audioController.playbackPositionSeconds(),
                            recordedDurationSeconds = state.recordedDurationSeconds,
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
        timerJob?.cancel()
        analysisJob?.cancel()
        audioController.release()
        super.onCleared()
    }

    private companion object {
        const val ANALYSIS_LOADING_DELAY_MILLIS = 3_000L
        const val TIMER_DELAY_MILLIS = 1_000L
        const val PLAYBACK_TIMER_DELAY_MILLIS = 250L
    }
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
