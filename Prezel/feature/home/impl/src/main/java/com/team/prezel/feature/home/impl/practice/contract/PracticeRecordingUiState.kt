package com.team.prezel.feature.home.impl.practice.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingState

@Immutable
internal data class PracticeRecordingUiState(
    val practiceScript: String = "",
    val recordingState: PracticeRecordingState = PracticeRecordingState.Idle,
    val analysisStatus: PracticeRecordingAnalysisStatus = PracticeRecordingAnalysisStatus.Ready,
) : UiState {
    val currentSeconds: Int
        get() = recordingState.currentSeconds

    val totalSeconds: Int
        get() = recordingState.totalSeconds

    val analyzeEnabled: Boolean
        get() = recordingState is PracticeRecordingState.ReadyToPlay &&
            analysisStatus !is PracticeRecordingAnalysisStatus.Loading
}
