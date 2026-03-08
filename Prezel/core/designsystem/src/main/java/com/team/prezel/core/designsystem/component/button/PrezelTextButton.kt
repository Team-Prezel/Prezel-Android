package com.team.prezel.core.designsystem.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: PrezelButtonStyle = PrezelButtonStyle(),
) {
    PrezelButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@ThemePreview
@Composable
private fun PrezelTextButtonPreviewFilled() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.FILLED, content = ::PrezelTextButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelTextButtonPreviewOutlined() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.OUTLINED, content = ::PrezelTextButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelTextButtonPreviewGhost() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.GHOST, content = ::PrezelTextButtonPreviewItem)
    }
}

@Composable
private fun PrezelTextButtonPreviewItem(
    style: PrezelButtonStyle,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    PrezelTextButton(
        text = "Label",
        onClick = {},
        enabled = enabled,
        style = style,
        modifier = modifier,
    )
}
