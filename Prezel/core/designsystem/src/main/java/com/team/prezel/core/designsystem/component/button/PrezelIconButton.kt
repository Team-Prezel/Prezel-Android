package com.team.prezel.core.designsystem.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelIconButton(
    icon: IconSource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: PrezelButtonStyle = PrezelButtonStyle(),
) {
    PrezelButton(
        icon = icon,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@ThemePreview
@Composable
private fun PrezelIconButtonPreviewFilled() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.FILLED, content = ::PrezelIconButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelIconButtonPreviewOutlined() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.OUTLINED, content = ::PrezelIconButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelIconButtonPreviewGhost() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.GHOST, content = ::PrezelIconButtonPreviewItem)
    }
}

@Composable
private fun PrezelIconButtonPreviewItem(
    style: PrezelButtonStyle,
    enabled: Boolean,
) {
    PrezelIconButton(
        icon = IconSource(resId = PrezelIcons.Blank),
        onClick = {},
        enabled = enabled,
        style = style,
    )
}
