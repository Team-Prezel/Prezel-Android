package com.team.prezel.feature.home.impl.practice

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingPhase
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingViewModel @Inject constructor(
    audioControllerFactory: PracticeRecordingAudioControllerFactory,
) : BaseViewModel<PracticeRecordingUiState, PracticeRecordingUiIntent, PracticeRecordingUiEffect>(
        PracticeRecordingUiState(),
    ) {
    private val audioController = audioControllerFactory.create()
    private var recordingFilePath: String? = null
    private var timerJob: Job? = null

    override fun onIntent(intent: PracticeRecordingUiIntent) {
        when (intent) {
            PracticeRecordingUiIntent.ClickControl -> onClickControl()
        }
    }

    private fun onClickControl() {
        when (currentState.phase) {
            PracticeRecordingPhase.IDLE -> startRecording()
            PracticeRecordingPhase.RECORDING -> stopRecording()
            PracticeRecordingPhase.RECORDED -> startPlayback()
            PracticeRecordingPhase.PLAYING -> stopPlayback()
        }
    }

    private fun startRecording() {
        recordingFilePath = audioController.startRecording()
        updateState {
            copy(
                phase = PracticeRecordingPhase.RECORDING,
                recordingSeconds = 0,
                playbackSeconds = 0,
                recordedDurationSeconds = 0,
            )
        }
        startRecordingTimer()
    }

    private fun stopRecording() {
        val durationSeconds = audioController.stopRecording()
        timerJob?.cancel()
        updateState {
            copy(
                phase = PracticeRecordingPhase.RECORDED,
                recordedDurationSeconds = durationSeconds.coerceAtLeast(recordingSeconds),
                playbackSeconds = 0,
            )
        }
    }

    private fun startPlayback() {
        val filePath = recordingFilePath ?: return
        val durationSeconds = audioController.startPlayback(filePath) {
            timerJob?.cancel()
            updateState {
                copy(
                    phase = PracticeRecordingPhase.RECORDED,
                    playbackSeconds = recordedDurationSeconds,
                )
            }
        }
        updateState {
            copy(
                phase = PracticeRecordingPhase.PLAYING,
                recordedDurationSeconds = durationSeconds.coerceAtLeast(recordedDurationSeconds),
                playbackSeconds = 0,
            )
        }
        startPlaybackTimer()
    }

    private fun stopPlayback() {
        audioController.stopPlayback()
        timerJob?.cancel()
        updateState {
            copy(
                phase = PracticeRecordingPhase.RECORDED,
                playbackSeconds = 0,
            )
        }
    }

    private fun startRecordingTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1_000)
                updateState { copy(recordingSeconds = recordingSeconds + 1) }
            }
        }
    }

    private fun startPlaybackTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(250)
                updateState { copy(playbackSeconds = audioController.playbackPositionSeconds()) }
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        audioController.release()
        super.onCleared()
    }
}
