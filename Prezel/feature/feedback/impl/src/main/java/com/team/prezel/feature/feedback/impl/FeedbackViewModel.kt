package com.team.prezel.feature.feedback.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationDetailUseCase
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
import timber.log.Timber

@HiltViewModel(assistedFactory = FeedbackViewModel.Factory::class)
internal class FeedbackViewModel @AssistedInject constructor(
    @Assisted private val navKey: FeedbackNavKey,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
    private val writeSelfFeedbackUseCase: WriteSelfFeedbackUseCase,
) : BaseViewModel<FeedbackUiState, FeedbackUiIntent, FeedbackUiEffect>(FeedbackUiState()) {
    @AssistedFactory
    interface Factory {
        fun create(navKey: FeedbackNavKey): FeedbackViewModel
    }

    private var initialContent: String = ""

    init {
        fetchInitialContent()
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

    private fun fetchInitialContent() {
        viewModelScope.launch {
            fetchPresentationDetailUseCase(
                presentationId = navKey.presentationId,
                isPast = navKey.isPast,
            ).onSuccess { result ->
                val selfFeedback = result.analysisSummary.selfFeedback
                    .orEmpty()
                initialContent = selfFeedback

                if (currentState.content.isBlank()) {
                    updateState { copy(content = selfFeedback) }
                }
            }.onFailure { throwable ->
                sendEffect(FeedbackUiEffect.ShowMessage(FeedbackUiMessage.FETCH_FEEDBACK_FAILED))
                Timber.e(throwable)
            }
        }
    }

    private fun changeContent(content: String) {
        updateState { copy(content = content) }
    }

    private fun handleClose() {
        if (hasUnsavedContent()) {
            updateState { copy(isExitDialogVisible = true) }
            return
        }

        navigateBack()
    }

    private fun hasUnsavedContent(): Boolean = currentState.content != initialContent

    private fun save() {
        val content = currentState.content.trim()
        if (content.isBlank() || currentState.isSaving) return

        updateState { copy(isSaving = true) }

        viewModelScope.launch {
            writeSelfFeedbackUseCase(
                presentationId = navKey.presentationId,
                content = content,
            ).onSuccess {
                sendEffect(FeedbackUiEffect.SaveComplete)
            }.onFailure { throwable ->
                updateState { copy(isSaving = false) }
                sendEffect(FeedbackUiEffect.ShowMessage(throwable.toFeedbackUiMessage()))
                Timber.e(throwable)
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { sendEffect(FeedbackUiEffect.NavigateBack) }
    }
}

private fun Throwable.toFeedbackUiMessage(): FeedbackUiMessage {
    val error = (this as? AppException)?.error

    return when (error) {
        AppError.UNAUTHORIZED -> FeedbackUiMessage.PRESENTATION_FORBIDDEN
        AppError.NOT_FOUND -> FeedbackUiMessage.PRESENTATION_NOT_FOUND
        AppError.DUPLICATE -> FeedbackUiMessage.SELF_FEEDBACK_ALREADY_WRITTEN
        AppError.INVALID_REQUEST,
        AppError.SERVER_ERROR,
        AppError.VOICE_RECOGNITION_FAILED,
        AppError.VOICE_ANALYSIS_FAILED,
        AppError.SCRIPT_FILE_RECOGNITION_FAILED,
        AppError.NETWORK,
        AppError.UNKNOWN,
        null,
        -> FeedbackUiMessage.SAVE_FAILED
    }
}
