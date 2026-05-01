package com.team.prezel.feature.home.impl.practice

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.practice.audio.PracticeRecordingAudioControllerFactory
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisErrorType
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingState
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingViewModel @Inject constructor(
    audioControllerFactory: PracticeRecordingAudioControllerFactory,
) : BaseViewModel<PracticeRecordingUiState, PracticeRecordingUiIntent, PracticeRecordingUiEffect>(PracticeRecordingUiState()) {
    private val audioController = audioControllerFactory.create()
    private var recordingFilePath: String? = null
    private var timerJob: Job? = null
    private var analysisJob: Job? = null

    override fun onIntent(intent: PracticeRecordingUiIntent) {
        when (intent) {
            PracticeRecordingUiIntent.ClickControl -> onClickControl()
            PracticeRecordingUiIntent.ClickAnalyze -> startAnalysis()
        }
    }

    private fun onClickControl() {
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
                viewModelScope.launch {
                    sendEffect(
                        PracticeRecordingUiEffect.ShowMessage(
                            PracticeRecordingUiMessage.RECORDING_START_FAILED,
                        ),
                    )
                }
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
                        analysisStatus = PracticeRecordingAnalysisStatus.Error(
                            PracticeRecordingAnalysisErrorType.VOICE_RECOGNITION_FAILED,
                        ),
                    )
                }
            }
    }

    private fun startPlayback() {
        val previousState = currentState.recordingState
        if (previousState !is PracticeRecordingState.Recorded) return

        val filePath = recordingFilePath ?: return

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
                        analysisStatus = PracticeRecordingAnalysisStatus.Error(
                            PracticeRecordingAnalysisErrorType.VOICE_RECOGNITION_FAILED,
                        ),
                    )
                }
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

        analysisJob?.cancel()
        analysisJob = viewModelScope.launch {
            updateState {
                copy(analysisStatus = PracticeRecordingAnalysisStatus.Loading)
            }

            delay(ANALYSIS_LOADING_DELAY_MILLIS)

            updateState {
                copy(analysisStatus = PracticeRecordingAnalysisStatus.Success)
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
