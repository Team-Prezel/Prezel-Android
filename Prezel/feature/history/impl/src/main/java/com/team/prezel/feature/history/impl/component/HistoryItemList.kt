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
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

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

@BasicPreview
@Composable
private fun HistoryItemListPreview() {
    PrezelTheme {
        HistoryItemList(
            items = persistentListOf(
                HistoryUiModel(
                    id = 1L,
                    dDay = 5,
                    date = LocalDate(2025, 10, 20),
                    title = "캡스톤서비스기획 중간고사 발표",
                    category = Category.EDUCATION,
                    purpose = Purpose.CONTENT_DELIVERY,
                    style = Style.PROFESSIONAL,
                    audience = Audience.EXPERT,
                ),
                HistoryUiModel(
                    id = 2L,
                    dDay = 7,
                    date = LocalDate(2025, 10, 22),
                    title = "IT동아리 대규모 세미나",
                    category = Category.REPORT,
                    purpose = Purpose.CONTENT_DELIVERY,
                    style = Style.FRIENDLY,
                    audience = Audience.GENERAL_AUDIENCE,
                ),
            ),
            onClickItem = { },
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        )
    }
}
