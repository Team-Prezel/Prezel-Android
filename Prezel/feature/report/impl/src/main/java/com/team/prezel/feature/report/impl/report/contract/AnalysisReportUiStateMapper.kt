package com.team.prezel.feature.report.impl.report.contract

import com.team.prezel.core.model.presentation.ExpectedQuestion
import com.team.prezel.core.model.presentation.PresentationDetailWithPracticeRecords
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
import com.team.prezel.feature.report.impl.report.model.GrowthGraphData
import com.team.prezel.feature.report.impl.report.model.GrowthGraphItemUiModel
import com.team.prezel.feature.report.impl.report.model.PracticeRecordsUiModel.Companion.toUiModel
import com.team.prezel.feature.report.impl.report.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.report.model.QuestionUiModel
import com.team.prezel.feature.report.impl.report.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.report.model.SpeedGraphData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun PresentationDetailWithPracticeRecords.toAnalysisReportUiState(isPast: Boolean): AnalysisReportUiState =
    AnalysisReportUiState.Content(
        presentationInfo = PresentationInfoUiModel(
            category = analysisSummary.category,
            title = analysisSummary.title,
            purpose = analysisSummary.purpose,
            style = analysisSummary.style,
            audience = analysisSummary.audience,
            analyzedAt = analysisSummary.analyzedAt,
            durationSeconds = analysisSummary.durationSeconds,
        ),
        summaryFeedback = analysisSummary.summaryFeedback,
        accuracyScore = analysisSummary.accuracyScore,
        scriptMatchRate = analysisSummary.scriptMatchRate,
        speedGraphData = SpeedGraphData(
            spm = analysisSummary.spm,
            result = analysisSummary.speedEvaluation,
        ),
        growthGraphData = analysisSummary.growth.toGrowthGraphData(),
        scriptAnalysisGraphData = ScriptAnalysisGraphData(
            spellingCount = analysisSummary.spellErrorCount,
            grammarCount = analysisSummary.grammarErrorCount,
            totalErrorCount = analysisSummary.totalErrorCount,
        ),
        expectedQuestions = analysisSummary.expectedQuestions.toQuestionUiModels(),
        selfFeedback = analysisSummary.selfFeedback,
        practiceRecords = practiceRecords?.toUiModel(),
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
