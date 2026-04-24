package com.team.prezel.feature.my.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.list.PrezelListSize
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.badge.BadgeType
import com.team.prezel.core.ui.component.PrezelBadge
import com.team.prezel.core.ui.component.drawableResId
import com.team.prezel.core.ui.component.title
import com.team.prezel.feature.my.impl.model.BadgeUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun BadgeSection(
    badges: ImmutableList<BadgeUiModel>,
    onClickBadge: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClickBadge,
                interactionSource = null,
                indication = null,
            ),
    ) {
        BadgeListTitle()

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        BadgeList(badges = badges)
    }
}

@Composable
private fun BadgeListTitle(modifier: Modifier = Modifier) {
    PrezelList(
        title = "나의 뱃지",
        titleTextColor = PrezelTheme.colors.textLarge,
        size = PrezelListSize.REGULAR,
        nested = true,
        modifier = modifier.padding(horizontal = PrezelTheme.spacing.V20),
        trailingContent = {
            Icon(
                painter = painterResource(PrezelIcons.ChevronRight),
                contentDescription = "뱃지",
                tint = PrezelTheme.colors.iconRegular,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BadgeList(
    badges: ImmutableList<BadgeUiModel>,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = PrezelTheme.spacing.V20
    val itemSpacing = PrezelTheme.spacing.V16

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val itemSize = remember(maxWidth, horizontalPadding, itemSpacing) {
            (maxWidth - horizontalPadding * 2 - itemSpacing) / 2
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            overscrollEffect = null,
        ) {
            items(items = badges, key = { badge -> badge.type }) { badge ->
                PrezelBadge(
                    title = badge.type.title(),
                    badgeResId = badge.type.drawableResId(),
                    isAchieved = badge.isAchieved,
                    modifier = Modifier.width(itemSize),
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun BadgeSectionPreview() {
    PrezelTheme {
        BadgeSection(
            badges = listOf(
                BadgeType.FIRST_PRESENTATION,
                BadgeType.SECOND_ANALYSIS,
                BadgeType.FIRST_PRACTICE,
                BadgeType.RETROSPECT_COMPLETED,
                BadgeType.PERFECT_SCORE,
                BadgeType.TEN_ANALYSIS,
            ).mapIndexed { index, type ->
                BadgeUiModel(type = type, isAchieved = index % 2 == 0)
            }.toImmutableList(),
            onClickBadge = {},
        )
    }
}
