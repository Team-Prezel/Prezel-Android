package com.team.prezel.feature.report.impl.analysis.contract

import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.report.api.model.ExpectedQuestionPayload
import com.team.prezel.feature.report.api.model.PresentationGrowthPointPayload
import com.team.prezel.feature.report.api.model.ReportAnalysisPayload
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphData
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphItemUiModel
import com.team.prezel.feature.report.impl.detail.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.detail.model.QuestionUiModel
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.detail.model.SpeedGraphData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun ReportAnalysisPayload.toAnalysisReportUiState(): AnalysisReportUiState =
    AnalysisReportUiState.Content(
        reportDetail = ReportDetailUiModel(
            presentationInfo = PresentationInfoUiModel(
                category = Category.from(value = category),
                title = title,
                purpose = Purpose.from(value = purpose),
                style = Style.from(value = style),
                audience = Audience.from(value = audience),
                analyzedAt = analyzedAt,
                durationSeconds = durationSeconds,
            ),
            summaryFeedback = summaryFeedback,
            accuracyScore = accuracyScore,
            scriptMatchRate = scriptMatchRate,
            speedGraphData = SpeedGraphData(
                spm = spm,
                result = RecordingSpeed.from(value = speedEvaluation),
            ),
            improvementGraphData = growth.toImprovementGraphData(),
            scriptAnalysisGraphData = ScriptAnalysisGraphData(
                spellingCount = spellErrorCount,
                grammarCount = grammarErrorCount,
            ),
            expectedQuestions = expectedQuestions.toQuestionUiModels(),
        ),
    )

private fun List<PresentationGrowthPointPayload>.toImprovementGraphData(): ImprovementGraphData =
    ImprovementGraphData(
        items = map { item ->
            ImprovementGraphItemUiModel(
                attempt = item.attempt,
                accuracyScore = item.accuracyScore,
                scriptMatchRate = item.scriptMatchRate,
            )
        }.toImmutableList(),
    )

private fun List<ExpectedQuestionPayload>.toQuestionUiModels(): ImmutableList<QuestionUiModel> =
    map { question ->
        QuestionUiModel(
            question = question.question,
            answer = question.answer,
        )
    }.toImmutableList()
