package com.team.prezel.feature.practice.impl.recording.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class PracticeRecordingUiState(
    val practiceScript: String = "",
    val recordingState: AudioSessionState = AudioSessionState.Idle,
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
        get() = recordingState is AudioSessionState.ReadyToPlay
}
