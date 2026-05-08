package com.team.prezel.feature.home.impl.practice.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingAnalysisStatus

@Immutable
internal data class PracticeRecordingUiState(
    val practiceScript: String = "",
    val recordingState: AudioSessionState = AudioSessionState.Idle,
    val analysisStatus: PracticeRecordingAnalysisStatus = PracticeRecordingAnalysisStatus.Ready,
) : UiState {
    val currentSeconds: Int
        get() = when (val state = recordingState) {
            AudioSessionState.Idle -> 0
            is AudioSessionState.Recording -> state.elapsedSeconds
            is AudioSessionState.ReadyToPlay -> state.positionSeconds
            is AudioSessionState.Playing -> state.positionSeconds
        }

    val totalSeconds: Int
        get() = when (val state = recordingState) {
            AudioSessionState.Idle,
            is AudioSessionState.Recording,
            -> 0

            is AudioSessionState.ReadyToPlay -> state.durationSeconds
            is AudioSessionState.Playing -> state.durationSeconds
        }

    val recordingFilePath: String?
        get() = when (val state = recordingState) {
            is AudioSessionState.ReadyToPlay -> state.source.filePath
            is AudioSessionState.Playing -> state.source.filePath
            else -> null
        }

    val analyzeEnabled: Boolean
        get() = recordingState is AudioSessionState.ReadyToPlay &&
            analysisStatus !is PracticeRecordingAnalysisStatus.Loading
}
