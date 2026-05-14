package com.team.prezel.feature.practice.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingUiIntent : UiIntent {
    data object ClickRecordingControl : PracticeRecordingUiIntent
}
