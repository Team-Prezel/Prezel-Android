package com.team.prezel.feature.practice.impl.recording.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.practice.impl.recording.model.PracticeRecordingUiMessage

internal sealed interface PracticeRecordingUiEffect : UiEffect {
    data class ShowMessage(
        val message: PracticeRecordingUiMessage,
    ) : PracticeRecordingUiEffect
}
