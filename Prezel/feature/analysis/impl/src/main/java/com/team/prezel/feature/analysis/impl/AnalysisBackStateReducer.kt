package com.team.prezel.feature.analysis.impl

import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm

internal val AnalysisFlowUiState.shouldResetAudioOnBack: Boolean
    get() =
        step == AnalysisFlowStep.VOICE_RECORDING ||
            step == AnalysisFlowStep.PRESENTATION_SCHEDULE ||
            reRecordingPresentationId != null ||
            reWritingScriptPresentationId != null

internal fun AnalysisFlowUiState.backClearedFormOrNull(): AnalysisForm? =
    when (step) {
        AnalysisFlowStep.PRESENTATION_SITUATION -> form.takeIf { it.hasSituationInput }?.clearSituationInput()
        AnalysisFlowStep.SCRIPT_INPUT -> form.takeIf { it.hasScriptInput }?.clearScriptInput()
        AnalysisFlowStep.PRESENTATION_SCHEDULE,
        AnalysisFlowStep.AUDIO_UPLOAD,
        AnalysisFlowStep.VOICE_RECORDING,
        AnalysisFlowStep.ANALYZING,
        AnalysisFlowStep.ANALYSIS_FAILED,
        AnalysisFlowStep.FILE_RECOGNITION_FAILED,
        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
        -> null
    }

private val AnalysisForm.hasSituationInput: Boolean
    get() = listOf(category, purpose, style, audience).any { it != null }

private val AnalysisForm.hasScriptInput: Boolean
    get() = script.isNotBlank() || scriptFileUri != null

private fun AnalysisForm.clearSituationInput(): AnalysisForm =
    copy(
        category = null,
        purpose = null,
        style = null,
        audience = null,
    )

private fun AnalysisForm.clearScriptInput(): AnalysisForm =
    copy(
        script = "",
        scriptFileUri = null,
    )
