package com.team.prezel.feature.home.impl.practice.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface PracticeRecordingState {
    val currentSeconds: Int
    val totalSeconds: Int
    val filePath: String?
        get() = null

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

    data class RecordingPaused(
        val recordingSeconds: Int,
    ) : PracticeRecordingState {
        override val currentSeconds: Int
            get() = recordingSeconds

        override val totalSeconds: Int = 0
    }

    data class ReadyToPlay(
        override val filePath: String,
        val durationSeconds: Int,
        val sourceType: SourceType,
    ) : PracticeRecordingState {
        override val currentSeconds: Int = 0

        override val totalSeconds: Int
            get() = durationSeconds
    }

    data class Playing(
        override val filePath: String,
        val playbackSeconds: Int,
        val durationSeconds: Int,
        val sourceType: SourceType,
    ) : PracticeRecordingState {
        override val currentSeconds: Int
            get() = playbackSeconds

        override val totalSeconds: Int
            get() = durationSeconds
    }

    data class PlaybackPaused(
        override val filePath: String,
        val playbackSeconds: Int,
        val durationSeconds: Int,
        val sourceType: SourceType,
    ) : PracticeRecordingState {
        override val currentSeconds: Int
            get() = playbackSeconds

        override val totalSeconds: Int
            get() = durationSeconds
    }

    enum class SourceType {
        RECORDED_FILE,
        EXTERNAL_FILE,
    }
}
