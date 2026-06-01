package com.team.prezel.feature.feedback.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface FeedbackUiIntent : UiIntent {
    data class ChangeContent(
        val content: String,
    ) : FeedbackUiIntent

    data object ClickClose : FeedbackUiIntent

    data object ClickSave : FeedbackUiIntent

    data object ClickDialogClose : FeedbackUiIntent

    data object ClickDialogExit : FeedbackUiIntent
}
