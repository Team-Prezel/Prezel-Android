package com.team.prezel.feature.feedback.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.WriteSelfFeedbackUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.feedback.api.FeedbackNavKey
import com.team.prezel.feature.feedback.impl.contract.FeedbackUiEffect
import com.team.prezel.feature.feedback.impl.contract.FeedbackUiIntent
import com.team.prezel.feature.feedback.impl.contract.FeedbackUiState
import com.team.prezel.feature.feedback.impl.model.FeedbackUiMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FeedbackViewModel.Factory::class)
internal class FeedbackViewModel @AssistedInject constructor(
    @Assisted private val navKey: FeedbackNavKey,
    private val writeSelfFeedbackUseCase: WriteSelfFeedbackUseCase,
) : BaseViewModel<FeedbackUiState, FeedbackUiIntent, FeedbackUiEffect>(FeedbackUiState()) {
    @AssistedFactory
    interface Factory {
        fun create(navKey: FeedbackNavKey): FeedbackViewModel
    }

    override fun onIntent(intent: FeedbackUiIntent) {
        when (intent) {
            is FeedbackUiIntent.ChangeContent -> changeContent(intent.content)
            FeedbackUiIntent.ClickClose -> handleClose()
            FeedbackUiIntent.ClickSave -> save()
            FeedbackUiIntent.ClickDialogClose -> updateState { copy(isExitDialogVisible = false) }
            FeedbackUiIntent.ClickDialogExit -> navigateBack()
        }
    }

    private fun changeContent(content: String) {
        updateState { copy(content = content.take(MAX_CONTENT_COUNT)) }
    }

    private fun handleClose() {
        if (currentState.hasUnsavedContent) {
            updateState { copy(isExitDialogVisible = true) }
            return
        }

        navigateBack()
    }

    private fun save() {
        val content = currentState.content.trim()
        if (content.isBlank() || currentState.isSaving) return

        updateState { copy(isSaving = true) }

        viewModelScope.launch {
            writeSelfFeedbackUseCase(
                presentationId = navKey.presentationId,
                content = content,
            ).onSuccess {
                sendEffect(FeedbackUiEffect.NavigateBack)
            }.onFailure {
                updateState { copy(isSaving = false) }
                sendEffect(FeedbackUiEffect.ShowMessage(FeedbackUiMessage.SAVE_FAILED))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { sendEffect(FeedbackUiEffect.NavigateBack) }
    }

    companion object {
        private const val MAX_CONTENT_COUNT = 200
    }
}
