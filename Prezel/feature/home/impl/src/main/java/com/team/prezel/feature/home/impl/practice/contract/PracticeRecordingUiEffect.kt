package com.team.prezel.feature.home.impl.practice.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingUiMessage

internal sealed interface PracticeRecordingUiEffect : UiEffect {
    data class ShowMessage(
        val message: PracticeRecordingUiMessage,
    ) : PracticeRecordingUiEffect
}
