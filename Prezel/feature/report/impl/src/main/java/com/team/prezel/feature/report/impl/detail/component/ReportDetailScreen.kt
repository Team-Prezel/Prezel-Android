package com.team.prezel.feature.report.impl.detail.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData

@Composable
internal fun ReportDetailScreen(
    reportDetail: ReportDetailUiModel,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    trailingActions: @Composable RowScope.() -> Unit = {},
    topSections: @Composable ColumnScope.() -> Unit = {},
    onDeleteClick: () -> Unit,
) {
    ReportDetailLayout(
        appBarTitle = reportDetail.presentationInfo.title,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcons = trailingActions,
        headerContent = { titleModifier ->
            ReportHeaderSection(
                info = reportDetail.presentationInfo,
                titleModifier = titleModifier,
            )
        },
        bodyContent = {
            topSections()
            ReportDetailSections(
                reportDetail = reportDetail,
                onDeleteClick = onDeleteClick,
                onImprovementCardIndexChange = {},
            )
        },
    )
}

@BasicPreview
@Composable
private fun ReportDetailScreenPreview() {
    PrezelTheme {
        ReportDetailScreen(
            reportDetail = ReportDetailPreviewData.reportDetail,
            onDeleteClick = {},
        )
    }
}
