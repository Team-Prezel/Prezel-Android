package com.team.prezel.feature.report.impl.report.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.component.body.AccuracySection
import com.team.prezel.feature.report.impl.report.component.body.ExpectedQuestionsSection
import com.team.prezel.feature.report.impl.report.component.body.GrowthGraphSection
import com.team.prezel.feature.report.impl.report.component.body.PracticeHistorySection
import com.team.prezel.feature.report.impl.report.component.body.ScriptAnalysisSection
import com.team.prezel.feature.report.impl.report.component.body.SelfFeedbackSection
import com.team.prezel.feature.report.impl.report.component.body.SummarySection
import com.team.prezel.feature.report.impl.report.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewUpcomingUiState
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ReportBodyContent(
    uiState: AnalysisReportUiState.Content,
    onDeleteClick: () -> Unit,
    onImprovementCardIndexChange: (Int) -> Unit,
    onReWriteScriptClick: () -> Unit,
    onReRecordingClick: () -> Unit,
    onFeedBackWriteClick: () -> Unit,
    onScriptAnalysisClick: () -> Unit,
    onSpeechAccuracyClick: () -> Unit,
    onScriptMatchClick: () -> Unit,
) {
    if (uiState.isPast && uiState.practiceRecords != null) {
        SelfFeedbackSection(
            selfFeedback = uiState.selfFeedback,
            onFeedBackWriteClick = onFeedBackWriteClick,
        )
        PracticeHistorySection(practiceRecords = uiState.practiceRecords)
    }

    SummarySection(summary = uiState.summaryFeedback)
    AccuracySection(
        accuracyScore = uiState.accuracyScore,
        scriptMatchRate = uiState.scriptMatchRate,
        speedGraphData = uiState.speedGraphData,
        onSpeechAccuracyClick = onSpeechAccuracyClick,
        onScriptMatchClick = onScriptMatchClick,
    )
    GrowthGraphSection(
        growthGraphData = uiState.growthGraphData,
        onCardIndexChange = onImprovementCardIndexChange,
        onReRecordingClick = onReRecordingClick,
    )
    ScriptAnalysisSection(
        isWrittenScript = uiState.isScriptWritten,
        scriptAnalysisGraphData = uiState.scriptAnalysisGraphData,
        onReWriteScriptClick = onReWriteScriptClick,
        onScriptAnalysisClick = onScriptAnalysisClick,
    )
    ExpectedQuestionsSection(questions = uiState.expectedQuestions)

    PrezelButton(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.feature_report_impl_delete),
        type = ButtonType.OUTLINED,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.SECONDARY,
        onClick = onDeleteClick,
    )
}

@BasicPreview
@Composable
private fun ReportBodyContentPreview() {
    var uiState by remember { mutableStateOf(ReportPreviewUpcomingUiState) }

    PrezelTheme {
        Column(
            modifier = Modifier
                .padding(PrezelTheme.spacing.V20)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(64.dp),
        ) {
            ReportBodyContent(
                uiState = previewReportDetail(uiState),
                onDeleteClick = { },
                onImprovementCardIndexChange = { index -> uiState = updateSelectedGrowthCard(uiState, index) },
                onReWriteScriptClick = {},
                onReRecordingClick = {},
                onFeedBackWriteClick = {},
                onScriptAnalysisClick = {},
                onSpeechAccuracyClick = {},
                onScriptMatchClick = {},
            )
        }
    }
}

private fun previewReportDetail(uiState: AnalysisReportUiState.Content): AnalysisReportUiState.Content =
    uiState.copy(
        growthGraphData = uiState.growthGraphData.copy(
            items = persistentListOf(),
            selectedItemIndex = 0,
        ),
    )

private fun updateSelectedGrowthCard(
    uiState: AnalysisReportUiState.Content,
    selectedIndex: Int,
): AnalysisReportUiState.Content =
    uiState.copy(
        growthGraphData = uiState.growthGraphData.copy(
            selectedItemIndex = selectedIndex,
        ),
    )
