package com.team.prezel.feature.home.impl.practice.model

import androidx.compose.runtime.Immutable

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
