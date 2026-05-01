package com.team.prezel.feature.home.impl.practice.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class PracticeRecordingUiState(
    val recordingState: PracticeRecordingState = PracticeRecordingState.Idle,
    val analysisStatus: PracticeRecordingAnalysisStatus = PracticeRecordingAnalysisStatus.Ready,
) : UiState {
    val currentSeconds: Int
        get() = recordingState.currentSeconds

    val totalSeconds: Int
        get() = recordingState.totalSeconds

    val analyzeEnabled: Boolean
        get() = recordingState is PracticeRecordingState.Recorded &&
            analysisStatus !is PracticeRecordingAnalysisStatus.Loading
}

@Immutable
internal sealed interface PracticeRecordingState {
    val currentSeconds: Int
    val totalSeconds: Int

    data object Idle : PracticeRecordingState {
        override val currentSeconds: Int = 0
        override val totalSeconds: Int = 0
    }

    data class Recording(
        val recordingSeconds: Int,
    ) : PracticeRecordingState {
        override val currentSeconds: Int
            get() = recordingSeconds

        override val totalSeconds: Int = 0
    }

    data class Recorded(
        val recordedDurationSeconds: Int,
    ) : PracticeRecordingState {
        override val currentSeconds: Int = 0

        override val totalSeconds: Int
            get() = recordedDurationSeconds
    }

    data class Playing(
        val playbackSeconds: Int,
        val recordedDurationSeconds: Int,
    ) : PracticeRecordingState {
        override val currentSeconds: Int
            get() = playbackSeconds

        override val totalSeconds: Int
            get() = recordedDurationSeconds
    }
}

@Immutable
internal sealed interface PracticeRecordingAnalysisStatus {
    data object Ready : PracticeRecordingAnalysisStatus

    data object Loading : PracticeRecordingAnalysisStatus

    data object Success : PracticeRecordingAnalysisStatus

    data class Error(
        val type: PracticeRecordingAnalysisErrorType,
    ) : PracticeRecordingAnalysisStatus
}

internal enum class PracticeRecordingAnalysisErrorType {
    VOICE_RECOGNITION_FAILED,
    ANALYSIS_FAILED,
}
