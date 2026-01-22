package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

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
                modifier = Modifier.padding(prezelButtonContentPadding(size = buttonSize)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PrezelButtonIcon(icon = icon, size = buttonSize)
                text?.let { Text(text = it) }
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewFilled() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.FILLED)
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewOutlined() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.OUTLINED)
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewGhost() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.GHOST)
    }
}

@Composable
private fun PrezelButtonPreviewByType(type: PrezelButtonType) {
    val variants = persistentListOf(
        true to false,
        true to true,
        false to true,
        false to false,
    )

    PreviewScaffold {
        Text(text = type.name, style = PrezelTheme.typography.title2Medium)
        variants.forEach { variant ->
            HorizontalDivider()
            PrezelButtonVariantSection(type = type, enabled = variant.first, isRounded = variant.second)
        }
    }
}

@Composable
private fun PrezelButtonVariantSection(
    type: PrezelButtonType,
    enabled: Boolean,
    isRounded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Hierarchy: Primary | Enabled: $enabled | Radius: $isRounded", style = PrezelTheme.typography.body3Medium)
        PrezelButtonPreviewHierarchyBlock(
            type = type,
            hierarchy = PrezelButtonHierarchy.PRIMARY,
            enabled = enabled,
            isRounded = isRounded,
        )
        Text(text = "Hierarchy: Secondary | Enabled: $enabled | Radius: $isRounded", style = PrezelTheme.typography.body3Medium)
        PrezelButtonPreviewHierarchyBlock(
            type = type,
            hierarchy = PrezelButtonHierarchy.SECONDARY,
            enabled = enabled,
            isRounded = isRounded,
        )
    }
}

@Composable
private fun PrezelButtonPreviewHierarchyBlock(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    isRounded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PrezelButton(
                text = "Label",
                icon = DrawableIcon(PrezelIcons.Blank),
                onClick = {},
                enabled = enabled,
                style = PrezelButtonStyle(
                    buttonType = type,
                    buttonHierarchy = hierarchy,
                    buttonSize = PrezelButtonSize.XSMALL,
                    isRounded = isRounded,
                ),
            )
            PrezelButton(
                text = "Label",
                icon = DrawableIcon(PrezelIcons.Blank),
                onClick = {},
                enabled = enabled,
                style = PrezelButtonStyle(
                    buttonType = type,
                    buttonHierarchy = hierarchy,
                    buttonSize = PrezelButtonSize.SMALL,
                    isRounded = isRounded,
                ),
            )
            PrezelButton(
                text = "Label",
                icon = DrawableIcon(PrezelIcons.Blank),
                onClick = {},
                enabled = enabled,
                style = PrezelButtonStyle(
                    buttonType = type,
                    buttonHierarchy = hierarchy,
                    buttonSize = PrezelButtonSize.REGULAR,
                    isRounded = isRounded,
                ),
            )
        }
    }
}
