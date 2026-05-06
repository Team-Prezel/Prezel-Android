package com.team.prezel.feature.home.impl.practice.contract

import android.net.Uri
import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingUiIntent : UiIntent {
    data object LoadPracticeScript : PracticeRecordingUiIntent

    data object RecordAudioPermissionDenied : PracticeRecordingUiIntent

    data object RecordAudioPermissionPermanentlyDenied : PracticeRecordingUiIntent

    data object StartRecording : PracticeRecordingUiIntent

    data object PauseRecording : PracticeRecordingUiIntent

    data object ResumeRecording : PracticeRecordingUiIntent

    data object StopRecording : PracticeRecordingUiIntent

    data object ResetRecording : PracticeRecordingUiIntent

    data class AudioFileSelected(
        val uri: Uri,
    ) : PracticeRecordingUiIntent

    data object StartPlayback : PracticeRecordingUiIntent

    data object PausePlayback : PracticeRecordingUiIntent

    data object ResumePlayback : PracticeRecordingUiIntent

    data object StopPlayback : PracticeRecordingUiIntent

    data object AnalyzeClicked : PracticeRecordingUiIntent
}
