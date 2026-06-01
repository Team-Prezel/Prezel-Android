package com.team.prezel.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.component.PrezelAsyncImage
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelBadge(
    title: String,
    url: String,
    isAchieved: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BadgeImage(imageUrl = url, isAchieved = isAchieved)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        Text(
            text = title,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textLarge,
        )
    }
}

@Composable
private fun BadgeImage(
    imageUrl: String,
    isAchieved: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape = PrezelTheme.shapes.V16),
    ) {
        PrezelAsyncImage(
            url = imageUrl,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        if (!isAchieved) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = PrezelTheme.colors.scrimContainer),
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
private fun PrezelBadgePreview() {
    PrezelTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrezelTheme.colors.bgRegular)
                .padding(PrezelTheme.spacing.V16),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        ) {
            PrezelBadge(
                title = "Achieved",
                url = "https://picsum.photos/200",
                isAchieved = true,
                modifier = Modifier.weight(1f),
            )
            PrezelBadge(
                title = "Locked",
                url = "https://picsum.photos/200",
                isAchieved = false,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
