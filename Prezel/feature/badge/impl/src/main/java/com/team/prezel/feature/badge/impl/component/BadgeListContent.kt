package com.team.prezel.feature.badge.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PrezelBadge
import com.team.prezel.feature.badge.impl.R
import com.team.prezel.feature.badge.impl.model.BadgeUiModel
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeListContent(
    badges: ImmutableList<BadgeUiModel>,
    onBack: () -> Unit,
    onBadgeClick: (badgeCode: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        PrezelTopAppBar(
            title = stringResource(R.string.feature_badge_impl_title),
        ) {
            LeadingIcon(
                iconResId = PrezelIcons.ChevronLeft,
                contentDescription = stringResource(R.string.feature_badge_impl_back),
                onClick = onBack,
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V16, horizontal = PrezelTheme.spacing.V20),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            overscrollEffect = null,
        ) {
            items(items = badges, key = { badge -> badge.badgeCode }) { badge ->
                PrezelBadge(
                    title = badge.badgeName,
                    url = badge.imageUrl,
                    isAchieved = badge.isUnlocked,
                    modifier = Modifier.clickable(
                        onClick = { onBadgeClick(badge.badgeCode) },
                        indication = ripple(color = PrezelTheme.colors.bgMedium),
                        interactionSource = null,
                    ),
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun BadgeListContentPreview() {
    PrezelTheme {
        BadgeListContent(
            badges = badgePreviewBadges(),
            onBack = {},
            onBadgeClick = {},
        )
    }
}
