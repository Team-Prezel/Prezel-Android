package com.team.prezel.feature.analysis.impl

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import com.team.prezel.feature.analysis.impl.contract.AnalysisSituationOption
import com.team.prezel.feature.analysis.impl.contract.ScriptInputType
import com.team.prezel.feature.analysis.impl.contract.recordingFilePath
import kotlinx.datetime.LocalDate

internal data class PresentationAnalysisSubmission(
    val name: String,
    val date: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val script: String?,
    val scriptFileUri: String?,
    val audioFileUri: String?,
    val recordingFilePath: String,
)

internal fun AnalysisFlowUiIntent.reduceFormOrNull(form: AnalysisForm): AnalysisForm? =
    when (this) {
        is AnalysisFlowUiIntent.UpdatePresentationTitle -> form.copy(presentationTitle = title)
        is AnalysisFlowUiIntent.UpdatePresentationDate -> form.copy(presentationDate = date)
        is AnalysisFlowUiIntent.SelectScriptInputType -> form.copy(
            scriptInputType = inputType,
            script = "",
            scriptFileUri = null,
        )
        is AnalysisFlowUiIntent.UpdateScript -> form.copy(script = script)
        is AnalysisFlowUiIntent.SelectSituationOption -> form.selectSituationOption(option)
        else -> null
    }

internal fun PresentationAnalysisSummary.toAnalysisForm(): AnalysisForm =
    AnalysisForm(
        presentationTitle = title,
        presentationDate = analyzedAt,
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
    )

internal fun AnalysisFlowUiState.toPresentationAnalysisSubmissionOrNull(): PresentationAnalysisSubmission? {
    val category = form.category ?: return null
    val purpose = form.purpose ?: return null
    val style = form.style ?: return null
    val audience = form.audience ?: return null
    val recordingFilePath = form.audioFileUri ?: recordingState.recordingFilePath ?: return null
    val isFileUpload = form.scriptInputType == ScriptInputType.FILE_UPLOAD

    return PresentationAnalysisSubmission(
        name = form.presentationTitle.trim(),
        date = form.presentationDate,
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
        script = form.script.takeIf { !isFileUpload && it.isNotBlank() },
        scriptFileUri = form.scriptFileUri.takeIf { isFileUpload && !it.isNullOrBlank() },
        audioFileUri = form.audioFileUri,
        recordingFilePath = recordingFilePath,
    )
}

internal fun String.toRequestDate(): String =
    runCatching {
        val (year, month, day) = split("년 ", "월 ", "일")

        LocalDate(
            year = year.toInt(),
            month = month.toInt(),
            day = day.toInt(),
        ).toString()
    }.getOrDefault(this)

private fun AnalysisForm.selectSituationOption(option: AnalysisSituationOption): AnalysisForm =
    when (option) {
        is AnalysisSituationOption.CategoryOption -> copy(category = option.category)
        is AnalysisSituationOption.PurposeOption -> copy(purpose = option.purpose)
        is AnalysisSituationOption.StyleOption -> copy(style = option.style)
        is AnalysisSituationOption.AudienceOption -> copy(audience = option.audience)
    }
