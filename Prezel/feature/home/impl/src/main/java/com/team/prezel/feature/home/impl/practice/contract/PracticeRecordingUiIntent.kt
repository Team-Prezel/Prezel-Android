package com.team.prezel.feature.home.impl.practice.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingUiIntent : UiIntent {
    data object LoadPracticeScript : PracticeRecordingUiIntent

    data object RecordAudioPermissionDenied : PracticeRecordingUiIntent

    data object RecordAudioPermissionPermanentlyDenied : PracticeRecordingUiIntent

    data object ToggleRecordingControl : PracticeRecordingUiIntent

    data object AnalyzeClicked : PracticeRecordingUiIntent
}
