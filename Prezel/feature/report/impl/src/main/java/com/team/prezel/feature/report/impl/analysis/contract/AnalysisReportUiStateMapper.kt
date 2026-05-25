package com.team.prezel.feature.report.impl.analysis.contract

import com.team.prezel.core.model.presentation.ExpectedQuestion
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphData
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphItemUiModel
import com.team.prezel.feature.report.impl.detail.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.detail.model.QuestionUiModel
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.detail.model.SpeedGraphData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun PresentationAnalysisSummary.toAnalysisReportUiState(): AnalysisReportUiState =
    AnalysisReportUiState.Content(
        reportDetail = ReportDetailUiModel(
            presentationInfo = PresentationInfoUiModel(
                category = category,
                title = title,
                purpose = purpose,
                style = style,
                audience = audience,
                analyzedAt = analyzedAt,
                durationSeconds = durationSeconds,
            ),
            summaryFeedback = summaryFeedback,
            accuracyScore = accuracyScore,
            scriptMatchRate = scriptMatchRate,
            speedGraphData = SpeedGraphData(
                spm = spm,
                result = speedEvaluation,
            ),
            improvementGraphData = growth.toImprovementGraphData(),
            scriptAnalysisGraphData = ScriptAnalysisGraphData(
                spellingCount = spellErrorCount,
                grammarCount = grammarErrorCount,
            ),
            expectedQuestions = expectedQuestions.toQuestionUiModels(),
        ),
    )

private fun List<PresentationGrowthPoint>.toImprovementGraphData(): ImprovementGraphData =
    ImprovementGraphData(
        items = map { item ->
            ImprovementGraphItemUiModel(
                attempt = item.attempt,
                accuracyScore = item.accuracyScore,
                scriptMatchRate = item.scriptMatchRate,
            )
        }.toImmutableList(),
    )

private fun List<ExpectedQuestion>.toQuestionUiModels(): ImmutableList<QuestionUiModel> =
    map { question ->
        QuestionUiModel(
            question = question.question,
            answer = question.answer,
        )
    }.toImmutableList()
