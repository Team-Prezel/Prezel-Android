package com.team.prezel.feature.report.impl.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData
import com.team.prezel.feature.report.impl.history.component.HistoryReportContent
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiIntent
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiState

@Composable
internal fun HistoryReportScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryReportScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onDeleteClick = { viewModel.onIntent(HistoryReportUiIntent.ClickDelete) },
        modifier = modifier,
    )
}

@Composable
internal fun HistoryReportScreenContent(
    uiState: HistoryReportUiState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is HistoryReportUiState.Content -> HistoryReportContent(
            state = uiState,
            onBackClick = onBackClick,
            onDeleteClick = onDeleteClick,
            modifier = modifier,
        )

        is HistoryReportUiState.Loading -> Unit
    }
}

@BasicPreview
@Composable
private fun HistoryReportScreenPreview() {
    PrezelTheme {
        HistoryReportScreenContent(
            uiState = ReportDetailPreviewData.historyState,
            onBackClick = { },
            onDeleteClick = { },
        )
    }
}
