package com.team.prezel.feature.analysis.impl.contract

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.base.UiIntent

internal sealed interface AnalysisFlowUiIntent : UiIntent {
    data class UpdatePresentationTitle(
        val title: String,
    ) : AnalysisFlowUiIntent

    data class UpdatePresentationDate(
        val date: String,
    ) : AnalysisFlowUiIntent

    data class SelectCategory(
        val category: Category,
    ) : AnalysisFlowUiIntent

    data class SelectPurpose(
        val purpose: Purpose,
    ) : AnalysisFlowUiIntent

    data class SelectStyle(
        val style: Style,
    ) : AnalysisFlowUiIntent

    data class SelectAudience(
        val audience: Audience,
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
    ) : AnalysisFlowUiIntent

    data object Next : AnalysisFlowUiIntent

    data object SkipScript : AnalysisFlowUiIntent

    data object Back : AnalysisFlowUiIntent
}
