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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel
import com.team.prezel.feature.badge.impl.model.BadgeUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeDetailModal(
    badge: BadgeUiModel,
    badgeDetail: BadgeDetailUiModel?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onDismiss)

    Column(
        modifier = modifier
            .background(PrezelTheme.colors.bgRegular)
            .noRippleClickable { /* 클릭 이벤트 소비를 위함 */ },
    ) {
        PrezelTopAppBar(
            trailingIcons = {
                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(PrezelIcons.Cancel),
                        contentDescription = "닫기",
                    )
                }
            },
        )

        Spacer(Modifier.weight(72f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(376f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BadgeDetailImage(
                imageUrl = badge.imageUrl,
                isUnlocked = badge.isUnlocked,
                modifier = Modifier.fillMaxWidth(0.65f),
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

            Text(
                text = badge.badgeName,
                style = PrezelTheme.typography.title1Bold,
                color = PrezelTheme.colors.textLarge,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

            if (isLoading) {
                CircularProgressIndicator(
                    color = PrezelTheme.colors.interactiveRegular,
                    modifier = Modifier.padding(top = PrezelTheme.spacing.V12),
                )
            } else {
                badgeDetail?.let { detail ->
                    BadgeDetailInfo(detail = detail)
                }
            }
        }
        Spacer(Modifier.weight(180f))
    }
}

@BasicPreview
@Composable
private fun BadgeDetailModalPreview() {
    PrezelTheme {
        BadgeDetailModal(
            badge = badgePreviewBadges().first(),
            badgeDetail = badgePreviewDetail(),
            isLoading = false,
            onDismiss = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
