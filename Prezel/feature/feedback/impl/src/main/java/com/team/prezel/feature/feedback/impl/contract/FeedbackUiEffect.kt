package com.team.prezel.feature.feedback.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.feedback.impl.model.FeedbackUiMessage

internal sealed interface FeedbackUiEffect : UiEffect {
    data object NavigateBack : FeedbackUiEffect

    data class ShowMessage(
        val message: FeedbackUiMessage,
    ) : FeedbackUiEffect
}
