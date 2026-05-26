package com.team.prezel.feature.history.impl.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.component.chip.chip.ChipHierarchy
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipState
import com.team.prezel.core.designsystem.component.chip.chip.ChipStatus
import com.team.prezel.core.designsystem.component.chip.chip.ChipType
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.history.impl.R
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.datetime.LocalDate

@Composable
internal fun HistoryPresentationCard(
    item: HistoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = PrezelTheme.shapes.V8)
                .background(PrezelTheme.colors.bgRegular)
                .padding(all = PrezelTheme.spacing.V14),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
        ) {
            HistoryPresentationCardHeader(
                dDayLabel = item.dDay,
                dateLabel = item.dateLabel,
            )
            HistoryPresentationCardTitle(title = item.title)
            HistoryPresentationCardChips(item = item)
        }
    }
}

@Composable
private fun HistoryPresentationCardHeader(
    dDayLabel: String,
    dateLabel: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dDayLabel,
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.interactiveRegular,
        )
        Text(
            text = dateLabel,
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelTheme.colors.textSmall,
        )
    }
}

@Composable
private fun HistoryPresentationCardTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = PrezelTheme.typography.body2Bold,
        color = PrezelTheme.colors.textLarge,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun HistoryPresentationCardChips(
    item: HistoryUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HistoryCategoryChip(text = stringResource(item.category.labelResId()), category = item.category)
        HistoryMetaChip(text = stringResource(item.purpose.labelResId()))
        HistoryMetaChip(text = stringResource(item.style.labelResId()))
        HistoryMetaChip(text = stringResource(item.audience.labelResId()))
    }
}

@Composable
private fun HistoryCategoryChip(
    text: String,
    category: Category,
    modifier: Modifier = Modifier,
) {
    PrezelChip(
        text = text,
        modifier = modifier,
        iconResId = category.iconResId(),
        type = ChipType.FILLED,
        size = ChipSize.SMALL,
        state = ChipState.ACTIVE,
        hierarchy = ChipHierarchy.PRIMARY,
    )
}

@Composable
private fun HistoryMetaChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    PrezelChip(
        text = text,
        modifier = modifier,
        type = ChipType.OUTLINED,
        size = ChipSize.SMALL,
        hierarchy = ChipHierarchy.PRIMARY,
        status = ChipStatus.DEFAULT,
    )
}

@StringRes
private fun Category.labelResId(): Int =
    when (this) {
        Category.EDUCATION -> R.string.feature_history_impl_category_education
        Category.WORK -> R.string.feature_history_impl_category_work
        Category.OFFER -> R.string.feature_history_impl_category_offer
        Category.EVENT -> R.string.feature_history_impl_category_event
    }

@DrawableRes
private fun Category.iconResId(): Int =
    when (this) {
        Category.EDUCATION -> PrezelIcons.College
        Category.WORK -> PrezelIcons.Company
        Category.OFFER -> PrezelIcons.Hand
        Category.EVENT -> PrezelIcons.Balloon
    }

@StringRes
private fun Purpose.labelResId(): Int =
    when (this) {
        Purpose.INFO -> R.string.feature_history_impl_purpose_info
        Purpose.UNDERSTANDING -> R.string.feature_history_impl_purpose_understanding
        Purpose.EMPATHY -> R.string.feature_history_impl_purpose_empathy
    }

@StringRes
private fun Style.labelResId(): Int =
    when (this) {
        Style.FORMAL -> R.string.feature_history_impl_style_formal
        Style.FRIENDLY -> R.string.feature_history_impl_style_friendly
        Style.CALM -> R.string.feature_history_impl_style_calm
        Style.CASUAL -> R.string.feature_history_impl_style_casual
    }

@StringRes
private fun Audience.labelResId(): Int =
    when (this) {
        Audience.GENERAL -> R.string.feature_history_impl_audience_general
        Audience.PROFESSIONAL -> R.string.feature_history_impl_audience_professional
        Audience.TEAMMATE -> R.string.feature_history_impl_audience_teammate
    }

@BasicPreview
@Composable
private fun HistoryPresentationCardPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier
                .background(color = PrezelTheme.colors.bgMedium)
                .padding(8.dp),
        ) {
            HistoryPresentationCard(
                item = HistoryUiModel(
                    presentationId = 1L,
                    title = "캡스톤서비스기획 중간고사 발표",
                    presentationDate = LocalDate(2025, 10, 20),
                    category = Category.EDUCATION,
                    purpose = Purpose.INFO,
                    style = Style.FORMAL,
                    audience = Audience.PROFESSIONAL,
                    dDay = "D-5",
                ),
                onClick = { },
            )
        }
    }
}
