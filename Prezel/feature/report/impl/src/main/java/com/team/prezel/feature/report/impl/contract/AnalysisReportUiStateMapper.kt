package com.team.prezel.feature.report.impl.contract

import com.team.prezel.core.model.presentation.ExpectedQuestion
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
import com.team.prezel.feature.report.impl.model.GrowthGraphData
import com.team.prezel.feature.report.impl.model.GrowthGraphItemUiModel
import com.team.prezel.feature.report.impl.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.model.QuestionUiModel
import com.team.prezel.feature.report.impl.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.model.SpeedGraphData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun PresentationAnalysisSummary.toAnalysisReportUiState(isPast: Boolean): AnalysisReportUiState =
    AnalysisReportUiState.Content(
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
        growthGraphData = growth.toGrowthGraphData(),
        scriptAnalysisGraphData = ScriptAnalysisGraphData(
            spellingCount = spellErrorCount,
            grammarCount = grammarErrorCount,
            totalErrorCount = totalErrorCount,
        ),
        expectedQuestions = expectedQuestions.toQuestionUiModels(),
        selfFeedback = selfFeedback,
        isPast = isPast,
    )

private fun List<PresentationGrowthPoint>.toGrowthGraphData(): GrowthGraphData =
    GrowthGraphData(
        items = map { item ->
            GrowthGraphItemUiModel(
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
