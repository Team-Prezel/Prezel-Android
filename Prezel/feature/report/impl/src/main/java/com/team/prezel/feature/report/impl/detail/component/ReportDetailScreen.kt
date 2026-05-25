package com.team.prezel.feature.report.impl.detail.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData

@Composable
internal fun ReportDetailScreen(
    reportDetail: ReportDetailUiModel,
    modifier: Modifier = Modifier,
    topSections: @Composable ColumnScope.() -> Unit = {},
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    ReportDetailLayout(
        appBarTitle = reportDetail.presentationInfo.title,
        modifier = modifier,
        leadingIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(PrezelIcons.ArrowLeft),
                    contentDescription = stringResource(R.string.feature_report_impl_back),
                    tint = PrezelTheme.colors.iconRegular,
                )
            }
        },
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
            onBackClick = {},
            onDeleteClick = {},
        )
    }
}
