package com.team.prezel.feature.analysis.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.base.UiIntent
import com.team.prezel.feature.analysis.api.AnalysisStartType

@Immutable
internal sealed interface AnalysisFlowUiIntent : UiIntent {
    data class EnterStep(
        val step: AnalysisFlowStep,
        val startType: AnalysisStartType,
    ) : AnalysisFlowUiIntent

    data class StartReRecording(
        val presentationId: Long,
        val isPast: Boolean,
    ) : AnalysisFlowUiIntent

    data class StartReWritingScript(
        val presentationId: Long,
        val isPast: Boolean,
    ) : AnalysisFlowUiIntent

    data class UpdatePresentationTitle(
        val title: String,
    ) : AnalysisFlowUiIntent

    data class UpdatePresentationDate(
        val date: String,
    ) : AnalysisFlowUiIntent

    data class SelectSituationOption(
        val option: AnalysisSituationOption,
    ) : AnalysisFlowUiIntent

    data class SelectScriptInputType(
        val inputType: ScriptInputType,
    ) : AnalysisFlowUiIntent

    data class UpdateScript(
        val script: String,
    ) : AnalysisFlowUiIntent

    data class SelectScriptFile(
        val fileUri: String?,
    ) : AnalysisFlowUiIntent

    data class SelectAudioFile(
        val fileUri: String?,
        val fileName: String? = null,
    ) : AnalysisFlowUiIntent

    data object ClickRecordingControl : AnalysisFlowUiIntent

    data object StopRecording : AnalysisFlowUiIntent

    data object ResetRecording : AnalysisFlowUiIntent

    data object Next : AnalysisFlowUiIntent

    data class RetryFileUpload(
        val uploadType: AnalysisUploadType,
    ) : AnalysisFlowUiIntent

    data object SkipScript : AnalysisFlowUiIntent

    data object Back : AnalysisFlowUiIntent

    companion object {
        fun selectSituationOption(category: Category?): AnalysisFlowUiIntent = SelectSituationOption(AnalysisSituationOption.CategoryOption(category))

        fun selectSituationOption(purpose: Purpose?): AnalysisFlowUiIntent = SelectSituationOption(AnalysisSituationOption.PurposeOption(purpose))

        fun selectSituationOption(style: Style?): AnalysisFlowUiIntent = SelectSituationOption(AnalysisSituationOption.StyleOption(style))

        fun selectSituationOption(audience: Audience?): AnalysisFlowUiIntent = SelectSituationOption(AnalysisSituationOption.AudienceOption(audience))
    }
}

internal enum class AnalysisUploadType {
    SCRIPT,
    AUDIO,
}

internal sealed interface AnalysisSituationOption {
    data class CategoryOption(
        val category: Category?,
    ) : AnalysisSituationOption

    data class PurposeOption(
        val purpose: Purpose?,
    ) : AnalysisSituationOption

    data class StyleOption(
        val style: Style?,
    ) : AnalysisSituationOption

    data class AudienceOption(
        val audience: Audience?,
    ) : AnalysisSituationOption
}
