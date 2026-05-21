package com.team.prezel.feature.report.impl.analysis.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.detail.component.ReportDetailScreen
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData

@Composable
internal fun AnalysisReportContent(
    state: AnalysisReportUiState.Content,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReportDetailScreen(
        reportDetail = state.reportDetail,
        modifier = modifier,
        trailingActions = {
            PrezelTextButton(
                text = stringResource(R.string.feature_report_impl_save),
                type = ButtonType.GHOST,
                hierarchy = ButtonHierarchy.SECONDARY,
                size = ButtonSize.SMALL,
                onClick = onSaveClick,
            )
        },
        onDeleteClick = onDeleteClick,
    )
}

@BasicPreview
@Composable
private fun AnalysisReportContentPreview() {
    PrezelTheme {
        AnalysisReportContent(
            state = ReportDetailPreviewData.analysisState,
            onSaveClick = {},
            onDeleteClick = {},
        )
    }
}
