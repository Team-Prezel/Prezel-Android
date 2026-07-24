package com.team.prezel.feature.report.impl.script

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.CorrectPresentationScriptUseCase
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationScriptDetailUseCase
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.ScriptCorrection
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.impl.refresh.ReportRefreshNotifier
import com.team.prezel.feature.report.impl.script.contract.ScriptUiEffect
import com.team.prezel.feature.report.impl.script.contract.ScriptUiIntent
import com.team.prezel.feature.report.impl.script.contract.ScriptUiState
import com.team.prezel.feature.report.impl.script.model.ScriptCorrectionUiModel
import com.team.prezel.feature.report.impl.script.model.ScriptUiMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ScriptViewModel.Factory::class)
internal class ScriptViewModel @AssistedInject constructor(
    @Assisted private val analysisResultId: Long,
    private val fetchPresentationScriptDetailUseCase: FetchPresentationScriptDetailUseCase,
    private val correctPresentationScriptUseCase: CorrectPresentationScriptUseCase,
    private val reportRefreshNotifier: ReportRefreshNotifier,
) : BaseViewModel<ScriptUiState, ScriptUiIntent, ScriptUiEffect>(ScriptUiState()) {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted analysisResultId: Long,
        ): ScriptViewModel
    }

    private var presentationId: Long? = null
    private var hasScriptChanged: Boolean = false

    init {
        fetchData(analysisResultId = analysisResultId)
    }

    override fun onIntent(intent: ScriptUiIntent) {
        when (intent) {
            ScriptUiIntent.ApplyAllCorrections -> applyAllCorrections()
            ScriptUiIntent.ClickClose -> handleCloseClick()
            ScriptUiIntent.ClickCopy -> copyCurrentScriptToClipboard()
            ScriptUiIntent.DismissCorrectionPopup -> dismissCorrectionPopup()

            is ScriptUiIntent.ClickCorrection -> selectCorrection(
                correctionId = intent.correctionId,
                popupY = intent.popupY,
            )

            is ScriptUiIntent.ApplyCorrection -> applyCorrection(
                correctionId = intent.correctionId,
            )
        }
    }

    private fun selectCorrection(
        correctionId: Long,
        popupY: Int,
    ) {
        if (currentState.isSubmittingCorrection) return

        updateState {
            copy(
                selectedCorrectionId = correctionId,
                selectedCorrectionPopupY = popupY,
            )
        }
    }

    private fun dismissCorrectionPopup() {
        updateState {
            copy(selectedCorrectionId = null)
        }
    }

    private fun fetchData(analysisResultId: Long) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            fetchPresentationScriptDetailUseCase(analysisResultId = analysisResultId)
                .onSuccess { detail ->
                    updateDetail(detail = detail)
                    updateState { copy(isLoading = false) }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    sendEffect(ScriptUiEffect.ShowMessage(ScriptUiMessage.FETCH_SCRIPT_DETAIL_FAILED))
                    sendEffect(ScriptUiEffect.NavigateToBack)
                }
        }
    }

    private fun applyCorrection(correctionId: Long) {
        if (currentState.isSubmittingCorrection) return

        val targetCorrection = currentState.scriptDetails.firstOrNull { correction ->
            correction.id == correctionId
        } ?: return

        submitCorrection(
            correctedIndices = listOf(targetCorrection.id.toInt()),
            finalScript = rebuildScript(
                originalScript = currentState.originalScript,
                corrections = listOf(targetCorrection),
            ),
        )
    }

    private fun applyAllCorrections() {
        if (currentState.isSubmittingCorrection) return
        if (currentState.scriptDetails.isEmpty()) return

        submitCorrection(
            correctedIndices = currentState.scriptDetails.map { correction -> correction.id.toInt() },
            finalScript = rebuildScript(
                originalScript = currentState.originalScript,
                corrections = currentState.scriptDetails,
            ),
        )
    }

    private fun copyCurrentScriptToClipboard() {
        viewModelScope.launch {
            sendEffect(
                ScriptUiEffect.CurrentScriptCopyToClipBoard(
                    script = currentState.currentScript,
                ),
            )
        }
    }

    private fun handleCloseClick() {
        if (currentState.isSubmittingCorrection) return

        if (hasScriptChanged) {
            presentationId?.let(reportRefreshNotifier::requestRefresh)
        }

        viewModelScope.launch {
            sendEffect(ScriptUiEffect.NavigateToBack)
        }
    }

    private fun submitCorrection(
        correctedIndices: List<Int>,
        finalScript: String,
    ) {
        viewModelScope.launch {
            updateState {
                copy(
                    isSubmittingCorrection = true,
                    selectedCorrectionId = null,
                    currentScript = finalScript,
                )
            }

            correctPresentationScriptUseCase(
                analysisResultId = analysisResultId,
                finalScript = finalScript,
                correctedIndices = correctedIndices,
            ).onSuccess { detail ->
                hasScriptChanged = true
                updateDetail(detail = detail)
                updateState { copy(isSubmittingCorrection = false) }
            }.onFailure {
                updateState { copy(isSubmittingCorrection = false, currentScript = originalScript) }
                sendEffect(ScriptUiEffect.ShowMessage(ScriptUiMessage.CORRECT_SCRIPT_FAILED))
            }
        }
    }

    private fun rebuildScript(
        originalScript: String,
        corrections: List<ScriptCorrectionUiModel>,
    ): String {
        val sortedCorrections = corrections.sortedBy { correction -> correction.originalRange.first }

        val builder = StringBuilder(originalScript)
        var offset = 0

        sortedCorrections.forEach { correction ->
            val startIndex = correction.originalRange.first + offset
            val endIndex = correction.originalRange.last + 1 + offset

            builder.replace(
                startIndex,
                endIndex,
                correction.correctedText,
            )

            offset += correction.correctedText.length - correction.originalText.length
        }

        return builder.toString()
    }

    private fun updateDetail(detail: PresentationScriptDetail) {
        presentationId = detail.presentationId
        updateState {
            copy(
                originalScript = detail.originalScript,
                currentScript = detail.originalScript,
                scriptDetails = detail.toCorrectionUiModels(),
                selectedCorrectionId = null,
            )
        }
    }
}

internal fun PresentationScriptDetail.toCorrectionUiModels(): ImmutableList<ScriptCorrectionUiModel> {
    return scriptCorrections
        .mapIndexedNotNull { index, correction ->
            val originalRange = correction.resolveOriginalRangeIn(originalScript) ?: return@mapIndexedNotNull null

            ScriptCorrectionUiModel(
                id = index.toLong(),
                errorType = correction.errorType,
                sentence = correction.sentence,
                originalText = correction.originalText,
                correctedText = correction.correctedText,
                reason = correction.reason,
                originalRange = originalRange,
            )
        }.toImmutableList()
}

private fun ScriptCorrection.resolveOriginalRangeIn(script: String): IntRange? {
    if (startIndex !in 0..endIndex) return null

    if (endIndex <= script.length && script.substring(startIndex, endIndex) == originalText) {
        return startIndex until endIndex
    }

    if (endIndex < script.length && script.substring(startIndex, endIndex + 1) == originalText) {
        return startIndex..endIndex
    }

    return null
}
