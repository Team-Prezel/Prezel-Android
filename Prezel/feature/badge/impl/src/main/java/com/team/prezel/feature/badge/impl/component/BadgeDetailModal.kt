package com.team.prezel.feature.badge.impl.component

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeDetailScreenContent(
    badgeDetail: BadgeDetailUiModel?,
    onBack: () -> Unit,
    onImageLoadFailure: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(PrezelTheme.colors.bgRegular)
            .noRippleClickable { /* 클릭 이벤트 소비를 위함 */ },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrezelTopAppBar(title = null) {
            TrailingIcon(
                iconResId = PrezelIcons.Cancel,
                contentDescription = stringResource(R.string.feature_badge_impl_back),
                onClick = onBack,
            )
        }

        Spacer(modifier = Modifier.weight(72f))

        badgeDetail?.let { detail ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BadgeHeader(
                    badgeDetail = detail,
                    onImageLoadFailure = onImageLoadFailure,
                )

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
    badgeDetail: BadgeDetailUiModel,
    onImageLoadFailure: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.65f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BadgeDetailImage(
            imageUrl = badgeDetail.imageUrl,
            isUnlocked = badgeDetail.isUnlocked,
            onError = onImageLoadFailure,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        Text(
            text = badgeDetail.badgeName,
            style = PrezelTheme.typography.title1Bold,
            color = PrezelTheme.colors.textLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

        BadgeDetailChip(badgeCondition = badgeDetail.conditionText)
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
private fun BadgeDetailScreenContentPreview() {
    PrezelTheme {
        BadgeDetailScreenContent(
            badgeDetail = badgePreviewDetail(),
            onBack = {},
            onImageLoadFailure = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
