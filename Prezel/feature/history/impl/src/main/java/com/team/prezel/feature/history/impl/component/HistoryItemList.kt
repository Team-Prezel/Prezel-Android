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
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.history.impl.model.HistoryPresentationStatus
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun HistoryItemList(
    items: ImmutableList<HistoryUiModel>,
    onClickItem: (HistoryUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = PrezelTheme.spacing.V20,
            vertical = PrezelTheme.spacing.V16,
        ),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V14),
    ) {
        items(items = items, key = { item -> item.id }) { item ->
            HistoryPresentationCard(
                item = item,
                onClick = { onClickItem(item) },
            )
        }
    }
}

private val previewItems = persistentListOf(
    HistoryUiModel(
        id = 1L,
        dDayLabel = "D-5",
        dateLabel = "2025.10.20",
        title = "캡스톤서비스기획 중간고사 발표",
        category = Category.EDUCATION,
        purpose = Purpose.CONTENT_DELIVERY,
        style = Style.PROFESSIONAL,
        audience = Audience.EXPERT,
        status = HistoryPresentationStatus.PREPARING,
    ),
    HistoryUiModel(
        id = 2L,
        dDayLabel = "D-7",
        dateLabel = "2025.10.22",
        title = "IT동아리 대규모 세미나",
        category = Category.REPORT,
        purpose = Purpose.CONTENT_DELIVERY,
        style = Style.FRIENDLY,
        audience = Audience.GENERAL_AUDIENCE,
        status = HistoryPresentationStatus.PREPARING,
    ),
)

@BasicPreview
@Composable
private fun HistoryItemListPreview() {
    PrezelTheme {
        HistoryItemList(
            items = previewItems,
            onClickItem = { },
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        )
    }
}
