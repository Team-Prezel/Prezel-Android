package com.team.prezel.feature.history.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.history.impl.model.HistoryUiMessage

internal sealed interface HistoryUiEffect : UiEffect {
    data class ShowMessage(
        val message: HistoryUiMessage,
    ) : HistoryUiEffect

    data class NavigateToReport(
        val presentationId: Long,
        val isPast: Boolean,
    ) : HistoryUiEffect
}
