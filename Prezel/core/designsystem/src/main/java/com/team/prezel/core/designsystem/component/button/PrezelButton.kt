package com.team.prezel.core.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.number.PrezelShapes
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

enum class PrezelButtonType {
    FILLED,
    OUTLINED,
    GHOST,
}

enum class PrezelButtonHierarchy {
    PRIMARY,
    SECONDARY,
}

enum class PrezelButtonSize {
    XSMALL,
    SMALL,
    REGULAR,
}

@Composable
fun PrezelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIconResId: Int? = null,
    enabled: Boolean = true,
    isRounded: Boolean = true,
    buttonType: PrezelButtonType = PrezelButtonType.FILLED,
    buttonHierarchy: PrezelButtonHierarchy = PrezelButtonHierarchy.PRIMARY,
    buttonSize: PrezelButtonSize = PrezelButtonSize.REGULAR,
) {
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
                PrezelButtonIcon(iconResId = leadingIconResId, size = buttonSize)
                Text(text = text)
            }
        }
    }
}

@Composable
private fun PrezelButtonIcon(
    @DrawableRes iconResId: Int?,
    size: PrezelButtonSize,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    if (iconResId == null) return
    Icon(
        painter = painterResource(iconResId),
        contentDescription = contentDescription,
        modifier = modifier.size(
            when (size) {
                PrezelButtonSize.XSMALL -> 14.dp
                PrezelButtonSize.SMALL -> 16.dp
                PrezelButtonSize.REGULAR -> 20.dp
            },
        ),
    )
    Spacer(
        modifier = Modifier.width(if (size == PrezelButtonSize.REGULAR) PrezelTheme.spacing.V8 else PrezelTheme.spacing.V4),
    )
}

@Composable
private fun prezelButtonShape(
    isRounded: Boolean,
    shapes: PrezelShapes = PrezelTheme.shapes,
): Shape = if (isRounded) shapes.V1000 else shapes.V4

@Composable
private fun prezelButtonBorderStroke(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    colors: PrezelColors = PrezelTheme.colors,
    stroke: PrezelStroke = PrezelTheme.stroke,
): BorderStroke {
    if (type != PrezelButtonType.OUTLINED) return BorderStroke(0.dp, Color.Transparent)
    if (!enabled) return BorderStroke(width = stroke.V1, color = colors.borderDisabled)

    val borderColor = when (hierarchy) {
        PrezelButtonHierarchy.PRIMARY -> colors.interactiveRegular
        PrezelButtonHierarchy.SECONDARY -> colors.borderMedium
    }

    return BorderStroke(width = stroke.V1, color = borderColor)
}

@Composable
private fun prezelButtonTextStyle(size: PrezelButtonSize): TextStyle =
    when (size) {
        PrezelButtonSize.XSMALL -> PrezelTheme.typography.caption2Medium
        PrezelButtonSize.SMALL -> PrezelTheme.typography.body3Medium
        PrezelButtonSize.REGULAR -> PrezelTheme.typography.body2Bold
    }

@Composable
private fun prezelButtonContainerColor(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    colors: PrezelColors = PrezelTheme.colors,
): Color =
    when (type) {
        PrezelButtonType.FILLED -> {
            if (!enabled) {
                colors.bgLarge
            } else if (hierarchy == PrezelButtonHierarchy.PRIMARY) {
                colors.interactiveRegular
            } else {
                colors.bgLarge
            }
        }

        PrezelButtonType.OUTLINED -> Color.Transparent
        PrezelButtonType.GHOST -> Color.Transparent
    }

@Composable
private fun prezelButtonContentColor(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    colors: PrezelColors = PrezelTheme.colors,
): Color {
    if (!enabled) return colors.textDisabled
    if (hierarchy == PrezelButtonHierarchy.SECONDARY) return colors.textMedium

    return when (type) {
        PrezelButtonType.FILLED -> if (isSystemInDarkTheme()) colors.textLarge else PrezelColorScheme.Dark.textLarge
        PrezelButtonType.OUTLINED -> colors.interactiveRegular
        PrezelButtonType.GHOST -> colors.interactiveRegular
    }
}

@Composable
private fun prezelButtonContentPadding(
    size: PrezelButtonSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues {
    val horizontal = when (size) {
        PrezelButtonSize.XSMALL -> spacing.V10
        PrezelButtonSize.SMALL -> spacing.V12
        PrezelButtonSize.REGULAR -> spacing.V16
    }

    val vertical = when (size) {
        PrezelButtonSize.XSMALL -> spacing.V6
        PrezelButtonSize.SMALL -> spacing.V8
        PrezelButtonSize.REGULAR -> spacing.V12
    }

    return PaddingValues(horizontal = horizontal, vertical = vertical)
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
                onClick = {},
                enabled = enabled,
                isRounded = isRounded,
                leadingIconResId = PrezelIcons.Blank,
                buttonType = type,
                buttonHierarchy = hierarchy,
                buttonSize = PrezelButtonSize.XSMALL,
            )
            PrezelButton(
                text = "Label",
                onClick = {},
                enabled = enabled,
                isRounded = isRounded,
                leadingIconResId = PrezelIcons.Blank,
                buttonType = type,
                buttonHierarchy = hierarchy,
                buttonSize = PrezelButtonSize.SMALL,
            )
            PrezelButton(
                text = "Label",
                onClick = {},
                enabled = enabled,
                isRounded = isRounded,
                leadingIconResId = PrezelIcons.Blank,
                buttonType = type,
                buttonHierarchy = hierarchy,
                buttonSize = PrezelButtonSize.REGULAR,
            )
        }
    }
}
