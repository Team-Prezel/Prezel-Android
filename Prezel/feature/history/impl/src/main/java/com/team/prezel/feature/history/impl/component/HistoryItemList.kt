package com.team.prezel.feature.history.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisStatus
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Composable
internal fun HistoryItemList(
    items: ImmutableList<HistoryUiModel>,
    onClickItem: (HistoryUiModel) -> Unit,
    modifier: Modifier = Modifier,
    itemEnabled: (HistoryUiModel) -> Boolean = { true },
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = PrezelTheme.spacing.V20,
            vertical = PrezelTheme.spacing.V16,
        ),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V14),
        overscrollEffect = null,
    ) {
        items(items = items, key = { item -> item.presentationId }) { item ->
            HistoryPresentationCard(
                item = item,
                onClick = { onClickItem(item) },
                enabled = itemEnabled(item),
            )
        }
    }
}

@BasicPreview
@Composable
private fun HistoryItemListPreview() {
    PrezelTheme {
        HistoryItemList(
            items = historyItemListPreviewItems(),
            onClickItem = { },
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        )
    }
}

private fun historyItemListPreviewItems(): ImmutableList<HistoryUiModel> =
    PresentationAnalysisStatus.entries
        .mapIndexed { index, status ->
            HistoryUiModel(
                presentationId = index.toLong(),
                title = status.previewTitle(),
                presentationDate = LocalDate(2025, 10, 20 + index),
                category = Category.EDUCATION,
                dDay = "D-${5 + index}",
                analysisStatus = status,
            )
        }.toImmutableList()

private fun PresentationAnalysisStatus.previewTitle(): String =
    when (this) {
        PresentationAnalysisStatus.IN_PROCESS -> "분석 진행 중 발표"
        PresentationAnalysisStatus.COMPLETE -> "분석 완료 발표"
        PresentationAnalysisStatus.DEFAULT -> "분석 전 발표"
        PresentationAnalysisStatus.FAIL -> "분석 실패 발표"
    }
