package com.team.prezel.feature.practice.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingUiIntent : UiIntent {
    data object StartRecording : PracticeRecordingUiIntent

    data object StopRecording : PracticeRecordingUiIntent

    data object StartPlayback : PracticeRecordingUiIntent

    data object StopPlayback : PracticeRecordingUiIntent

    data object AnalyzeRecording : PracticeRecordingUiIntent

    data object ResetRecording : PracticeRecordingUiIntent
}
