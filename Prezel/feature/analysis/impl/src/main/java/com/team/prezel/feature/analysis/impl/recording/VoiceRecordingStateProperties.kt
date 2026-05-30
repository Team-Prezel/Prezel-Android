package com.team.prezel.feature.analysis.impl.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.designsystem.component.voice.VoiceChromeStatus
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R
import java.util.Locale

internal val AudioSessionState.currentSeconds: Int
    get() = when (this) {
        AudioSessionState.Idle -> 0
        is AudioSessionState.Recording -> elapsedSeconds
        is AudioSessionState.PausedRecording -> elapsedSeconds
        is AudioSessionState.ReadyToPlay -> positionSeconds
        is AudioSessionState.Playing -> positionSeconds
    }

internal val AudioSessionState.totalSeconds: Int
    get() = when (this) {
        AudioSessionState.Idle,
        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> 0

        is AudioSessionState.ReadyToPlay -> durationSeconds
        is AudioSessionState.Playing -> durationSeconds
    }

internal val AudioSessionState.isCompleted: Boolean
    get() = this is AudioSessionState.ReadyToPlay || this is AudioSessionState.Playing

internal val AudioSessionState.recordingStatusSpacing
    @Composable get() = if (isCompleted) PrezelTheme.spacing.V12 else PrezelTheme.spacing.V8

internal val AudioSessionState.titleResId: Int
    get() = when (this) {
        AudioSessionState.Idle -> R.string.feature_analysis_impl_voice_recording_ready_title
        is AudioSessionState.Recording -> R.string.feature_analysis_impl_voice_recording_recording_title
        is AudioSessionState.PausedRecording -> R.string.feature_analysis_impl_voice_recording_paused_title
        is AudioSessionState.ReadyToPlay -> R.string.feature_analysis_impl_voice_recording_playing_title
        is AudioSessionState.Playing -> R.string.feature_analysis_impl_voice_recording_playing_title
    }

internal fun AudioSessionState.toVoiceChromeStatus(): VoiceChromeStatus =
    when (this) {
        is AudioSessionState.Recording -> VoiceChromeStatus.LISTENING
        is AudioSessionState.PausedRecording -> VoiceChromeStatus.WAITING
        is AudioSessionState.Playing -> VoiceChromeStatus.LISTENING
        is AudioSessionState.ReadyToPlay -> VoiceChromeStatus.WAITING
        AudioSessionState.Idle -> VoiceChromeStatus.IDLE
    }

internal val AudioSessionState.actionIconResId: Int
    get() = when (this) {
        AudioSessionState.Idle -> PrezelIcons.Recording
        is AudioSessionState.Recording -> PrezelIcons.Pause
        is AudioSessionState.PausedRecording -> PrezelIcons.Recording
        is AudioSessionState.ReadyToPlay -> PrezelIcons.Play
        is AudioSessionState.Playing -> PrezelIcons.Pause
    }

internal val AudioSessionState.actionIconColor: Color
    @Composable get() = when (this) {
        AudioSessionState.Idle -> PrezelTheme.colors.feedbackBadRegular
        is AudioSessionState.PausedRecording -> PrezelTheme.colors.feedbackBadRegular
        is AudioSessionState.Recording,
        is AudioSessionState.ReadyToPlay,
        is AudioSessionState.Playing,
        -> PrezelTheme.colors.iconRegular
    }

internal fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}
