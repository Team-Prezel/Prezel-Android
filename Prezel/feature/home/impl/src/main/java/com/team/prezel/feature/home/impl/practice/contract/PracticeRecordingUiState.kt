package com.team.prezel.feature.home.impl.practice.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

internal enum class PracticeRecordingPhase {
    IDLE,
    RECORDING,
    RECORDED,
    PLAYING,
}

internal sealed interface PracticeRecordingAnalysisStatus {
    data object Ready : PracticeRecordingAnalysisStatus

    data object Loading : PracticeRecordingAnalysisStatus

    data object Success : PracticeRecordingAnalysisStatus

    data class Error(
        val type: PracticeRecordingAnalysisErrorType,
    ) : PracticeRecordingAnalysisStatus
}

internal enum class PracticeRecordingAnalysisErrorType {
    ANALYZE,
    VOICE,
}

@Immutable
internal data class PracticeRecordingUiState(
    val phase: PracticeRecordingPhase = PracticeRecordingPhase.IDLE,
    val analysisStatus: PracticeRecordingAnalysisStatus = PracticeRecordingAnalysisStatus.Ready,
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
