package com.team.prezel.core.audio

sealed interface AudioSessionEffect {
    data object RecordingStartFailed : AudioSessionEffect

    data object RecordingStopFailed : AudioSessionEffect

    data object PlaybackStartFailed : AudioSessionEffect
}
