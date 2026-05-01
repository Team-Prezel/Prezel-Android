package com.team.prezel.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.badge.BadgeType
import com.team.prezel.core.ui.R

@Composable
fun PrezelBadge(
    title: String,
    @DrawableRes badgeResId: Int,
    isAchieved: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BadgeImage(resId = badgeResId, isAchieved = isAchieved)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        Text(
            text = title,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textLarge,
        )
    }
}

@Composable
fun BadgeType.title(): String =
    when (this) {
        BadgeType.FIRST_PRESENTATION -> R.string.core_ui_impl_badge_first_presentation
        BadgeType.SECOND_ANALYSIS -> R.string.core_ui_impl_badge_second_analysis
        BadgeType.FIRST_PRACTICE -> R.string.core_ui_impl_badge_first_practice
        BadgeType.RETROSPECT_COMPLETED -> R.string.core_ui_impl_badge_retrospect_completed
        BadgeType.PERFECT_SCORE -> R.string.core_ui_impl_badge_perfect_score
        BadgeType.TEN_ANALYSIS -> R.string.core_ui_impl_badge_ten_analysis
    }.let { resId -> stringResource(resId) }

// todo: 뱃지 디자인 업데이트 완료 후 반영할 예정
@Composable
fun BadgeType.drawableResId(): Int =
    when (this) {
        BadgeType.FIRST_PRESENTATION -> R.drawable.badge_start
        BadgeType.SECOND_ANALYSIS -> R.drawable.badge_start
        BadgeType.FIRST_PRACTICE -> R.drawable.badge_start
        BadgeType.RETROSPECT_COMPLETED -> R.drawable.badge_start
        BadgeType.PERFECT_SCORE -> R.drawable.badge_start
        BadgeType.TEN_ANALYSIS -> R.drawable.badge_start
    }

@Composable
private fun BadgeImage(
    @DrawableRes resId: Int,
    isAchieved: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape = PrezelTheme.shapes.V16),
    ) {
        Image(
            painter = painterResource(resId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        if (!isAchieved) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = PrezelTheme.colors.bgScrim),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(PrezelIcons.Lock),
                    contentDescription = null,
                    tint = PrezelTheme.colors.solidWhite,
                    modifier = Modifier.fillMaxSize(0.24f),
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelBadgeAchievedPreview() {
    val badgeType = BadgeType.FIRST_PRESENTATION
    PrezelTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            PrezelBadge(
                badgeResId = badgeType.drawableResId(),
                title = badgeType.title(),
                isAchieved = true,
                modifier = Modifier.width(100.dp),
            )
        }
    }
}

@BasicPreview
@Composable
private fun PrezelBadgeNotAchievedPreview() {
    val badgeType = BadgeType.FIRST_PRESENTATION
    PrezelTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            PrezelBadge(
                badgeResId = badgeType.drawableResId(),
                title = badgeType.title(),
                isAchieved = false,
                modifier = Modifier.width(100.dp),
            )
        }
    }
}
