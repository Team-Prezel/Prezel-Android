package com.team.prezel.feature.home.impl.practice.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingUiIntent : UiIntent {
    data object ClickControl : PracticeRecordingUiIntent
}
