package com.team.prezel.core.designsystem.component.avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class PrezelAvatarSize {
    REGULAR,
    SMALL,
}

@Immutable
sealed interface PrezelAvatarType {
    data object Default : PrezelAvatarType

    data class Image(
        val url: String,
    ) : PrezelAvatarType
}

@Composable
internal fun prezelAvatarContainerSize(size: PrezelAvatarSize): Dp =
    when (size) {
        PrezelAvatarSize.REGULAR -> 120.dp
        PrezelAvatarSize.SMALL -> 64.dp
    }

@Composable
internal fun prezelAvatarIconSize(size: PrezelAvatarSize): Dp =
    when (size) {
        PrezelAvatarSize.REGULAR -> 48.dp
        PrezelAvatarSize.SMALL -> 24.dp
    }

@Composable
internal fun prezelAvatarBorderWidth(size: PrezelAvatarSize): Dp =
    when (size) {
        PrezelAvatarSize.REGULAR -> PrezelTheme.stroke.V4
        PrezelAvatarSize.SMALL -> PrezelTheme.stroke.V2
    }
