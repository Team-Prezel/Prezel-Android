package com.team.prezel.feature.home.impl.practice.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

internal enum class PracticeRecordingPhase {
    IDLE,
    RECORDING,
    RECORDED,
    PLAYING,
}

@Immutable
internal data class PracticeRecordingUiState(
    val phase: PracticeRecordingPhase = PracticeRecordingPhase.IDLE,
    val recordingSeconds: Int = 0,
    val playbackSeconds: Int = 0,
    val recordedDurationSeconds: Int = 0,
) : UiState {
    val currentSeconds: Int
        get() = when (phase) {
            PracticeRecordingPhase.IDLE,
            PracticeRecordingPhase.RECORDING,
            -> recordingSeconds

            PracticeRecordingPhase.RECORDED,
            PracticeRecordingPhase.PLAYING,
            -> playbackSeconds
        }

    val totalSeconds: Int
        get() = when (phase) {
            PracticeRecordingPhase.IDLE,
            PracticeRecordingPhase.RECORDING,
            -> 0

            PracticeRecordingPhase.RECORDED,
            PracticeRecordingPhase.PLAYING,
            -> recordedDurationSeconds
        }

    val analyzeEnabled: Boolean
        get() = recordedDurationSeconds > 0 && phase != PracticeRecordingPhase.RECORDING
}
