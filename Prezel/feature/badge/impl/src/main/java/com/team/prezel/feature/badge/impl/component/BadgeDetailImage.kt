package com.team.prezel.feature.badge.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelAsyncImage
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun BadgeDetailImage(
    imageUrl: String,
    isUnlocked: Boolean,
    onError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var hasReportedError by remember(imageUrl) { mutableStateOf(false) }

    LaunchedEffect(imageUrl) {
        hasReportedError = false
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(PrezelTheme.shapes.V16),
    ) {
        PrezelAsyncImage(
            url = imageUrl,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onError = {
                if (!hasReportedError) {
                    hasReportedError = true
                    onError()
                }
            },
        )

        if (!isUnlocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PrezelTheme.colors.scrimContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(PrezelIcons.Lock),
                    contentDescription = null,
                    tint = PrezelTheme.colors.solidWhite,
                    modifier = Modifier.size(56.dp),
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun BadgeDetailImagePreview() {
    PrezelTheme {
        BadgeDetailImage(
            imageUrl = "",
            isUnlocked = false,
            onError = {},
        )
    }
}
