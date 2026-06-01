package com.team.prezel.feature.badge.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel

@Composable
internal fun BadgeDetailInfo(
    detail: BadgeDetailUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(PrezelTheme.shapes.V4)
                .background(PrezelTheme.colors.interactiveXSmall)
                .padding(horizontal = PrezelTheme.spacing.V6, vertical = PrezelTheme.spacing.V4),
        ) {
            Text(
                text = detail.conditionText,
                style = PrezelTheme.typography.caption2Regular,
                color = PrezelTheme.colors.interactiveRegular,
            )
        }

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        Text(
            text = detail.detailDescription,
            style = PrezelTheme.typography.body2Regular,
            color = PrezelTheme.colors.textLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@BasicPreview
@Composable
private fun BadgeDetailInfoPreview() {
    PrezelTheme {
        BadgeDetailInfo(detail = badgePreviewDetail())
    }
}
