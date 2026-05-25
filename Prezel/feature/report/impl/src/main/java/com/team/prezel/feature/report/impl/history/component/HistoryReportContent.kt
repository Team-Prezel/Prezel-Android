package com.team.prezel.feature.report.impl.history.component

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
import com.team.prezel.feature.report.impl.detail.component.ReportDetailScreen
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
internal fun HistoryReportContent(
    state: HistoryReportUiState.Content,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReportDetailScreen(
        reportDetail = state.reportDetail,
        modifier = modifier,
        leadingIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(PrezelIcons.ArrowLeft),
                    contentDescription = stringResource(R.string.feature_report_impl_back),
                )
            }
        },
        topSections = {
            SelfFeedbackSection(selfFeedback = state.selfFeedback)
            // todo 임시로 시간 설정함 -> API 추가되면 수정
            PracticeHistorySection(
                presentationDate = Clock.System
                    .now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
                    .plus(10, DateTimeUnit.DAY),
                practices = state.practices,
            )
        },
        onDeleteClick = onDeleteClick,
    )
}

@BasicPreview
@Composable
private fun HistoryReportContentPreview() {
    PrezelTheme {
        HistoryReportContent(
            state = ReportDetailPreviewData.historyState,
            onBackClick = { },
            onDeleteClick = { },
        )
    }
}
