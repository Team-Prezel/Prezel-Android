package com.team.prezel.feature.badge.impl.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.chip.chip.ChipHierarchy
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipState
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.badge.impl.R
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel
import com.team.prezel.feature.badge.impl.model.BadgeUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeDetailModal(
    badge: BadgeUiModel,
    badgeDetail: BadgeDetailUiModel?,
    onDismiss: () -> Unit,
    onImageLoadFailure: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onDismiss)

    Column(
        modifier = modifier
            .background(PrezelTheme.colors.bgRegular)
            .noRippleClickable { /* 클릭 이벤트 소비를 위함 */ },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrezelTopAppBar(
            title = "",
        ) {
            TrailingIcon(
                iconResId = PrezelIcons.Cancel,
                contentDescription = stringResource(R.string.feature_badge_impl_close),
                onClick = onDismiss,
            )
        }

        Spacer(modifier = Modifier.weight(72f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BadgeHeader(
                badge = badge,
                badgeDetail = badgeDetail,
                onImageLoadFailure = onImageLoadFailure,
            )

            badgeDetail?.let { detail ->
                Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
                BadgeDetailDescription(
                    detail = detail,
                    modifier = Modifier.padding(horizontal = PrezelTheme.spacing.V24),
                )
            }
        }

        Spacer(modifier = Modifier.weight(180f))
    }
}

@Composable
private fun BadgeHeader(
    badge: BadgeUiModel,
    badgeDetail: BadgeDetailUiModel?,
    onImageLoadFailure: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.65f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BadgeDetailImage(
            imageUrl = badge.imageUrl,
            isUnlocked = badge.isUnlocked,
            onError = onImageLoadFailure,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        Text(
            text = badge.badgeName,
            style = PrezelTheme.typography.title1Bold,
            color = PrezelTheme.colors.textLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

        badgeDetail?.let { detail ->
            BadgeDetailChip(badgeCondition = detail.conditionText)
        }
    }
}

@Composable
private fun BadgeDetailChip(
    badgeCondition: String,
    modifier: Modifier = Modifier,
) {
    PrezelChip(
        text = badgeCondition,
        size = ChipSize.SMALL,
        hierarchy = ChipHierarchy.PRIMARY,
        state = ChipState.ACTIVE,
        modifier = modifier,
    )
}

@Composable
private fun BadgeDetailDescription(
    detail: BadgeDetailUiModel,
    modifier: Modifier = Modifier,
) {
    Text(
        text = detail.detailDescription,
        style = PrezelTheme.typography.body2Regular,
        color = PrezelTheme.colors.textLarge,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth(),
    )
}

@BasicPreview
@Composable
private fun BadgeDetailModalPreview() {
    PrezelTheme {
        BadgeDetailModal(
            badge = badgePreviewBadges().first(),
            badgeDetail = badgePreviewDetail(),
            onDismiss = {},
            onImageLoadFailure = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
