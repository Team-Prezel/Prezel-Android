package com.team.prezel.feature.report.impl.report.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.report.impl.report.model.AnalysisReportDialog
import com.team.prezel.feature.report.impl.report.model.GrowthGraphData
import com.team.prezel.feature.report.impl.report.model.PracticeRecordsUiModel
import com.team.prezel.feature.report.impl.report.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.report.model.QuestionUiModel
import com.team.prezel.feature.report.impl.report.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.report.model.SpeedGraphData
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface AnalysisReportUiState : UiState {
    data object Loading : AnalysisReportUiState

    data class Content(
        val presentationInfo: PresentationInfoUiModel,
        val summaryFeedback: String,
        val accuracyScore: Double?,
        val scriptMatchRate: Double?,
        val speedGraphData: SpeedGraphData,
        val growthGraphData: GrowthGraphData,
        val scriptAnalysisGraphData: ScriptAnalysisGraphData,
        val expectedQuestions: ImmutableList<QuestionUiModel>,
        val selfFeedback: String?,
        val isPast: Boolean,
        val practiceRecords: PracticeRecordsUiModel,
        val reportDialog: AnalysisReportDialog? = null,
    ) : AnalysisReportUiState {
        val isScriptWritten: Boolean = accuracyScore != null && scriptMatchRate != null
    }
}
