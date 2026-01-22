package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: IconSource? = null,
    enabled: Boolean = true,
    style: PrezelButtonStyle = PrezelButtonStyle(),
) {
    if (text == null && icon == null) error("Button must have text or icon")
    val (buttonType, buttonHierarchy, buttonSize, isRounded) = style

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        enabled = enabled,
        shape = prezelButtonShape(isRounded = isRounded),
        color = prezelButtonContainerColor(type = buttonType, hierarchy = buttonHierarchy, enabled = enabled),
        border = prezelButtonBorderStroke(type = buttonType, hierarchy = buttonHierarchy, enabled = enabled),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides prezelButtonTextStyle(buttonSize),
            LocalContentColor provides prezelButtonContentColor(type = buttonType, hierarchy = buttonHierarchy, enabled = enabled),
        ) {
            Row(
                modifier = Modifier.padding(prezelButtonContentPadding(size = buttonSize, onlyIcon = text == null && icon != null)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PrezelButtonIcon(icon = icon, size = buttonSize)
                text?.let {
                    Spacer(modifier = Modifier.width(if (buttonSize == PrezelButtonSize.REGULAR) PrezelTheme.spacing.V8 else PrezelTheme.spacing.V4))
                    Text(text = it)
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewFilled() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.FILLED, content = ::PrezelButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewOutlined() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.OUTLINED, content = ::PrezelButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewGhost() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.GHOST, content = ::PrezelButtonPreviewItem)
    }
}

@Composable
private fun PrezelButtonPreviewItem(
    style: PrezelButtonStyle,
    enabled: Boolean,
) {
    PrezelButton(
        text = "Label",
        icon = DrawableIcon(PrezelIcons.Blank),
        onClick = {},
        enabled = enabled,
        style = style,
    )
}
