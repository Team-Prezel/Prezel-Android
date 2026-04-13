package com.team.prezel.feature.history.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.team.prezel.core.designsystem.component.chip.PrezelChip
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefaults
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipSize
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.history.impl.model.HistoryChipUiModel
import com.team.prezel.feature.history.impl.model.PresentationItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun HistoryItemList(
    items: ImmutableList<PresentationItem>,
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
            HistoryPresentationCard(item = item)
        }
    }
}

@Composable
private fun HistoryPresentationCard(
    item: PresentationItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = PrezelTheme.shapes.V8)
            .background(PrezelTheme.colors.bgRegular)
            .padding(all = PrezelTheme.spacing.V14),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.dDayLabel,
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.interactiveRegular,
            )
            Text(
                text = item.dateLabel,
                style = PrezelTheme.typography.caption1Regular,
                color = PrezelTheme.colors.textSmall,
            )
        }

        Text(
            text = item.title,
            style = PrezelTheme.typography.body2Bold,
            color = PrezelTheme.colors.textLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item.chips.forEach { chip ->
                HistoryChip(chip = chip)
            }
        }
    }
}

@BasicPreview
@Composable
private fun HistoryItemListPreview() {
    PrezelTheme {
        HistoryItemList(
            items = previewItems,
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        )
    }
}

private val previewItems = persistentListOf(
    PresentationItem(
        id = 1L,
        dDayLabel = "D-5",
        dateLabel = "2025.10.20",
        title = "캡스톤서비스기획 중간고사 발표",
        chips = persistentListOf(
            HistoryChipUiModel(label = "학술·교육", highlighted = true),
            HistoryChipUiModel(label = "내용 전달"),
            HistoryChipUiModel(label = "논리적"),
            HistoryChipUiModel(label = "전문가"),
        ),
    ),
    PresentationItem(
        id = 2L,
        dDayLabel = "D-7",
        dateLabel = "2025.10.22",
        title = "IT동아리 대규모 세미나",
        chips = persistentListOf(
            HistoryChipUiModel(label = "업무·보고", highlighted = true),
            HistoryChipUiModel(label = "내용 전달"),
            HistoryChipUiModel(label = "논리적"),
            HistoryChipUiModel(label = "일반 청중"),
        ),
    ),
)

@Composable
private fun HistoryChip(
    chip: HistoryChipUiModel,
    modifier: Modifier = Modifier,
) {
    if (chip.highlighted) {
        PrezelChip(
            text = chip.label,
            modifier = modifier,
            iconResId = PrezelIcons.Blank,
            config = PrezelChipDefaults.getDefault(
                iconOnly = false,
                type = PrezelChipType.FILLED,
                size = PrezelChipSize.SMALL,
                containerColor = PrezelTheme.colors.interactiveXSmall,
                iconColor = PrezelTheme.colors.interactiveRegular,
                textColor = PrezelTheme.colors.interactiveRegular,
            ),
        )
    } else {
        PrezelChip(
            text = chip.label,
            modifier = modifier,
            type = PrezelChipType.OUTLINED,
            size = PrezelChipSize.SMALL,
        )
    }
}
