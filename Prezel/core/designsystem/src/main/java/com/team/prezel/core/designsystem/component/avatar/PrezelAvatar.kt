package com.team.prezel.core.designsystem.component.avatar

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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.component.PrezelAsyncImage
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelAvatar(
    type: PrezelAvatarType,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: PrezelAvatarSize = PrezelAvatarSize.SMALL,
) {
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(prezelAvatarContainerSize(size))
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.bgRegular)
            .border(
                width = prezelAvatarBorderWidth(size),
                color = PrezelTheme.colors.borderRegular,
                shape = PrezelTheme.shapes.V1000,
            ),
        contentAlignment = Alignment.Center,
    ) {
        when (type) {
            is PrezelAvatarType.Default -> {
                DefaultAvatarIcon(
                    size = size,
                    contentDescription = contentDescription,
                )
            }

            is PrezelAvatarType.Image -> {
                if (isError) {
                    DefaultAvatarIcon(
                        size = size,
                        contentDescription = contentDescription,
                    )
                } else {
                    PrezelAsyncImage(
                        url = type.url,
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxSize(),
                        onFailure = { isError = true },
                    )
                }
            }
        }
    }
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
        tint = PrezelTheme.colors.borderMedium,
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
                    type = PrezelAvatarType.Default,
                    contentDescription = "기본 아바타",
                    size = PrezelAvatarSize.SMALL,
                )
                PrezelAvatar(
                    type = PrezelAvatarType.Default,
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
                    type = PrezelAvatarType.Default,
                    contentDescription = "Default Type",
                    size = PrezelAvatarSize.SMALL,
                )
                PrezelAvatar(
                    type = PrezelAvatarType.Image(url = "http://picsum.photos/200"),
                    contentDescription = "Image Type",
                    size = PrezelAvatarSize.SMALL,
                )
            }
        }
    }
}
