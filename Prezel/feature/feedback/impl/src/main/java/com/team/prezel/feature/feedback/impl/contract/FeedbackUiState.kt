package com.team.prezel.feature.feedback.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class FeedbackUiState(
    val content: String = "",
    val isSaving: Boolean = false,
    val isExitDialogVisible: Boolean = false,
) : UiState {
    val isSaveEnabled: Boolean
        get() = content.isNotBlank() && !isSaving
}
