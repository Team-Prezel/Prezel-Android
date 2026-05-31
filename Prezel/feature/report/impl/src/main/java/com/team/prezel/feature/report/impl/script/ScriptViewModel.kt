package com.team.prezel.feature.report.impl.script

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationScriptDetailUseCase
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.ui.base.BaseViewModel
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
) : BaseViewModel<ScriptUiState, ScriptUiIntent, ScriptUiEffect>(ScriptUiState()) {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted analysisResultId: Long,
        ): ScriptViewModel
    }

    init {
        fetchData(analysisResultId = analysisResultId)
    }

    override fun onIntent(intent: ScriptUiIntent) {
        when (intent) {
            ScriptUiIntent.ApplyAllCorrections -> applyAllCorrections()
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
                    updateState {
                        copy(
                            isLoading = false,
                            originalScript = detail.originalScript,
                            currentScript = detail.originalScript,
                            scriptDetails = detail.toCorrectionUiModels(),
                        )
                    }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    sendEffect(ScriptUiEffect.ShowMessage(ScriptUiMessage.FETCH_SCRIPT_DETAIL_FAILED))
                    sendEffect(ScriptUiEffect.NavigateToBack)
                }
        }
    }

    private fun applyCorrection(correctionId: Long) {
        val updatedCorrections = currentState.scriptDetails
            .map { correction ->
                if (correction.id != correctionId) return@map correction
                if (correction.isApplied) return@map correction

                correction.copy(isApplied = true)
            }.toImmutableList()

        val updatedScript = rebuildScript(
            originalScript = currentState.originalScript,
            corrections = updatedCorrections,
        )

        updateState {
            copy(
                currentScript = updatedScript,
                scriptDetails = updatedCorrections,
                selectedCorrectionId = null,
            )
        }
    }

    private fun applyAllCorrections() {
        val updatedCorrections = currentState.scriptDetails
            .map { correction ->
                correction.copy(isApplied = true)
            }.toImmutableList()

        val updatedScript = rebuildScript(
            originalScript = currentState.originalScript,
            corrections = updatedCorrections,
        )

        updateState {
            copy(
                currentScript = updatedScript,
                scriptDetails = updatedCorrections,
            )
        }
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

    private fun PresentationScriptDetail.toCorrectionUiModels(): ImmutableList<ScriptCorrectionUiModel> {
        return scriptCorrections
            .mapIndexedNotNull { index, correction ->
                val sentenceStartIndex = originalScript.indexOf(correction.sentence)

                if (sentenceStartIndex == -1) return@mapIndexedNotNull null

                val sentenceEndIndex = sentenceStartIndex + correction.sentence.length

                val correctionStartIndex = originalScript.indexOf(
                    string = correction.originalText,
                    startIndex = sentenceStartIndex,
                )

                if (correctionStartIndex == -1) return@mapIndexedNotNull null
                if (correctionStartIndex >= sentenceEndIndex) return@mapIndexedNotNull null

                ScriptCorrectionUiModel(
                    id = index.toLong(),
                    errorType = correction.errorType,
                    sentence = correction.sentence,
                    originalText = correction.originalText,
                    correctedText = correction.correctedText,
                    reason = correction.reason,
                    originalRange = correctionStartIndex until correctionStartIndex + correction.originalText.length,
                )
            }.toImmutableList()
    }

    private fun rebuildScript(
        originalScript: String,
        corrections: List<ScriptCorrectionUiModel>,
    ): String {
        val appliedCorrections = corrections
            .filter { correction -> correction.isApplied }
            .sortedBy { correction -> correction.originalRange.first }

        val builder = StringBuilder(originalScript)
        var offset = 0

        appliedCorrections.forEach { correction ->
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
}
