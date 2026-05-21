package com.team.prezel.feature.report.impl.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ColumnScope.ReportDetailSections(
    reportDetail: ReportDetailUiModel,
    onDeleteClick: () -> Unit,
    onImprovementCardIndexChange: (Int) -> Unit,
) {
    SummarySection(summary = reportDetail.summaryFeedback)
    AccuracySection(
        accuracyScore = reportDetail.accuracyScore,
        scriptMatchRate = reportDetail.scriptMatchRate,
        speedGraphData = reportDetail.speedGraphData,
    )
    GrowthGraphSection(
        improvementGraphData = reportDetail.improvementGraphData,
        onCardIndexChange = onImprovementCardIndexChange,
    )
    ScriptAnalysisSection(scriptAnalysisGraphData = reportDetail.scriptAnalysisGraphData)
    ExpectedQuestionsSection(questions = reportDetail.expectedQuestions)
    ReportBottomActionArea(onDeleteClick = onDeleteClick)
}

@BasicPreview
@Composable
private fun ReportDetailSectionsPreview() {
    var reportDetail by remember { mutableStateOf(ReportDetailPreviewData.reportDetail) }

    PrezelTheme {
        Column(
            modifier = Modifier
                .padding(PrezelTheme.spacing.V20)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(64.dp),
        ) {
            ReportDetailSections(
                reportDetail = previewReportDetail(reportDetail),
                onDeleteClick = { },
                onImprovementCardIndexChange = { index -> reportDetail = updateSelectedGrowthCard(reportDetail, index) },
            )
        }
    }
}

private fun previewReportDetail(reportDetail: ReportDetailUiModel): ReportDetailUiModel =
    reportDetail.copy(
        improvementGraphData = reportDetail.improvementGraphData.copy(
            items = persistentListOf(),
        ),
    )

private fun updateSelectedGrowthCard(
    reportDetail: ReportDetailUiModel,
    selectedIndex: Int,
): ReportDetailUiModel =
    reportDetail.copy(
        improvementGraphData = reportDetail.improvementGraphData.copy(
            selectedItemIndex = selectedIndex,
        ),
    )
