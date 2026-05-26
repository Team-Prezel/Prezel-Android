package com.team.prezel.feature.analysis.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class AnalysisFlowUiState(
    val step: AnalysisFlowStep = AnalysisFlowStep.PRESENTATION_SCHEDULE,
    val form: AnalysisForm = AnalysisForm(),
    val recordingState: AudioSessionState = AudioSessionState.Idle,
) : UiState {
    val progress: Float
        get() = when (step) {
            AnalysisFlowStep.PRESENTATION_SCHEDULE -> 0.25f
            AnalysisFlowStep.PRESENTATION_SITUATION,
            AnalysisFlowStep.SCRIPT_INPUT,
            AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
            -> 0.5f

            AnalysisFlowStep.AUDIO_UPLOAD,
            AnalysisFlowStep.VOICE_RECORDING,
            -> 0.75f

            AnalysisFlowStep.ANALYZING,
            AnalysisFlowStep.FILE_RECOGNITION_FAILED,
            -> 1f
        }

    val canMoveNext: Boolean
        get() = when (step) {
            AnalysisFlowStep.PRESENTATION_SCHEDULE ->
                form.presentationTitle.trim().length >= 2 && form.presentationDate.isNotBlank()

            AnalysisFlowStep.PRESENTATION_SITUATION -> {
                form.category != null && form.purpose != null && form.style != null && form.audience != null
            }

            AnalysisFlowStep.SCRIPT_INPUT -> when (form.scriptInputType) {
                ScriptInputType.FILE_UPLOAD -> !form.scriptFileUri.isNullOrBlank()
                ScriptInputType.DIRECT_INPUT -> form.script.isNotBlank()
            }

            AnalysisFlowStep.AUDIO_UPLOAD -> !form.audioFileUri.isNullOrBlank()
            AnalysisFlowStep.VOICE_RECORDING -> recordingState.recordingFilePath != null
            AnalysisFlowStep.ANALYZING,
            AnalysisFlowStep.FILE_RECOGNITION_FAILED,
            AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
            -> false
        }
}

@Immutable
internal data class AnalysisForm(
    val presentationTitle: String = "",
    val presentationDate: String = "",
    val category: Category? = null,
    val purpose: Purpose? = null,
    val style: Style? = null,
    val audience: Audience? = null,
    val scriptInputType: ScriptInputType = ScriptInputType.FILE_UPLOAD,
    val script: String = "",
    val scriptFileUri: String? = null,
    val audioFileUri: String? = null,
)

internal enum class ScriptInputType {
    FILE_UPLOAD,
    DIRECT_INPUT,
}

internal enum class AnalysisFlowStep {
    PRESENTATION_SCHEDULE,
    PRESENTATION_SITUATION,
    SCRIPT_INPUT,
    AUDIO_UPLOAD,
    VOICE_RECORDING,
    ANALYZING,
    FILE_RECOGNITION_FAILED,
    SCRIPT_FILE_RECOGNITION_FAILED,
}

internal val AudioSessionState.recordingFilePath: String?
    get() = when (this) {
        is AudioSessionState.ReadyToPlay -> source.filePath
        is AudioSessionState.Playing -> source.filePath
        AudioSessionState.Idle,
        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> null
    }
