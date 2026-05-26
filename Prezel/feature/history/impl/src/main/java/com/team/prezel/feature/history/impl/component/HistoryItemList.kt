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
        overscrollEffect = null,
    ) {
        items(items = items, key = { item -> item.presentationId }) { item ->
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
                    presentationId = 1L,
                    title = "캡스톤서비스기획 중간고사 발표",
                    presentationDate = LocalDate(2025, 10, 20),
                    category = Category.EDUCATION,
                    purpose = Purpose.INFO,
                    style = Style.FORMAL,
                    audience = Audience.PROFESSIONAL,
                    dDay = "D-5",
                ),
                HistoryUiModel(
                    presentationId = 2L,
                    title = "IT동아리 대규모 세미나",
                    presentationDate = LocalDate(2025, 10, 22),
                    category = Category.WORK,
                    purpose = Purpose.INFO,
                    style = Style.FRIENDLY,
                    audience = Audience.GENERAL,
                    dDay = "D-7",
                ),
            ),
            onClickItem = { },
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        )
    }
}
