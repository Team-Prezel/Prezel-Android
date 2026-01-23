package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelAvatar(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: PrezelAvatarSize = PrezelAvatarSize.SMALL,
) {
    var isError by remember(imageUrl) { mutableStateOf(false) }
    val shape = PrezelTheme.shapes.V1000

    Box(
        modifier = modifier
            .size(size = prezelAvatarContainerSize(size))
            .clip(shape = shape)
            .background(color = PrezelTheme.colors.bgRegular)
            .border(
                width = prezelAvatarBorderWidth(size),
                color = PrezelTheme.colors.borderRegular,
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        val shouldShowDefault =
            imageUrl.isNullOrBlank() || isError

        if (shouldShowDefault) {
            DefaultAvatarIcon(
                size = size,
                contentDescription = contentDescription,
            )
        } else {
            PrezelAsyncImage(
                url = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                onError = { isError = true },
                contentScale = ContentScale.Crop,
            )
        }
    }
}

enum class PrezelAvatarSize {
    REGULAR,
    SMALL,
}

private fun prezelAvatarContainerSize(size: PrezelAvatarSize): Dp =
    when (size) {
        PrezelAvatarSize.REGULAR -> 120.dp
        PrezelAvatarSize.SMALL -> 64.dp
    }

private fun prezelAvatarIconSize(size: PrezelAvatarSize): Dp =
    when (size) {
        PrezelAvatarSize.REGULAR -> 64.dp
        PrezelAvatarSize.SMALL -> 32.dp
    }

@Composable
private fun prezelAvatarBorderWidth(
    size: PrezelAvatarSize,
    stroke: PrezelStroke = PrezelTheme.stroke,
): Dp =
    when (size) {
        PrezelAvatarSize.REGULAR -> stroke.V4
        PrezelAvatarSize.SMALL -> stroke.V2
    }

@Composable
private fun DefaultAvatarIcon(
    size: PrezelAvatarSize,
    contentDescription: String,
) {
    Icon(
        painter = painterResource(R.drawable.ic_person),
        contentDescription = contentDescription,
        modifier = Modifier.size(prezelAvatarIconSize(size)),
        tint = PrezelTheme.colors.iconDisabled,
    )
}

@ThemePreview
@Composable
private fun PrezelAvatarSizePreview() {
    PrezelTheme {
        Surface(color = PrezelTheme.colors.bgRegular) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                PrezelAvatar(
                    imageUrl = null,
                    contentDescription = "기본 아바타",
                    size = PrezelAvatarSize.SMALL,
                )
                PrezelAvatar(
                    imageUrl = null,
                    contentDescription = "기본 아바타",
                    size = PrezelAvatarSize.REGULAR,
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelAvatarTypePreview() {
    PrezelTheme {
        Surface(color = PrezelTheme.colors.bgRegular) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                PrezelAvatar(
                    imageUrl = null,
                    contentDescription = "Default Type",
                    size = PrezelAvatarSize.SMALL,
                )
                PrezelAvatar(
                    imageUrl = "https://picsum.photos/200",
                    contentDescription = "Image Type",
                    size = PrezelAvatarSize.SMALL,
                )
            }
        }
    }
}
