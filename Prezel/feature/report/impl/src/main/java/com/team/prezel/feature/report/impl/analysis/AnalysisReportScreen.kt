package com.team.prezel.feature.report.impl.analysis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.analysis.component.AnalysisReportContent
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiEffect
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiIntent
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData

@Composable
internal fun AnalysisReportScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalysisReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AnalysisReportUiEffect.NavigateHome -> navigateToHome()
            }
        }
    }

    AnalysisReportScreenContent(
        uiState = uiState,
        onSaveClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickSave) },
        onDeleteClick = navigateToHome,
        modifier = modifier,
    )
}

@Composable
internal fun AnalysisReportScreenContent(
    uiState: AnalysisReportUiState,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is AnalysisReportUiState.Content -> AnalysisReportContent(
            state = uiState,
            onSaveClick = onSaveClick,
            onDeleteClick = onDeleteClick,
            modifier = modifier,
        )

        AnalysisReportUiState.Loading -> Unit
    }
}

@BasicPreview
@Composable
private fun AnalysisReportScreenPreview() {
    PrezelTheme {
        AnalysisReportScreenContent(
            uiState = ReportDetailPreviewData.analysisState,
            onSaveClick = { },
            onDeleteClick = { },
        )
    }
}

@BasicPreview
@Composable
private fun AnalysisReportScreenLoadingPreview() {
    PrezelTheme {
        AnalysisReportScreenContent(
            uiState = AnalysisReportUiState.Loading,
            onSaveClick = { },
            onDeleteClick = { },
        )
    }
}
