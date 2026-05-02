package com.team.prezel.feature.home.impl.practice.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingUiIntent : UiIntent {
    data object LoadPracticeScript : PracticeRecordingUiIntent

    data object DenyRecordAudioPermission : PracticeRecordingUiIntent

    data object DenyRecordAudioPermissionPermanently : PracticeRecordingUiIntent

    data object ClickControl : PracticeRecordingUiIntent

    data object ClickAnalyze : PracticeRecordingUiIntent
}
