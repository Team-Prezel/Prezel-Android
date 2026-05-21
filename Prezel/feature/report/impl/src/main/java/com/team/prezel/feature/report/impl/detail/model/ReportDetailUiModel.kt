package com.team.prezel.feature.report.impl.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class ReportDetailUiModel(
    val presentationInfo: PresentationInfoUiModel,
    val summaryFeedback: String,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
    val speedGraphData: SpeedGraphData,
    val improvementGraphData: ImprovementGraphData,
    val scriptAnalysisGraphData: ScriptAnalysisGraphData,
    val expectedQuestions: ImmutableList<QuestionUiModel>,
)
