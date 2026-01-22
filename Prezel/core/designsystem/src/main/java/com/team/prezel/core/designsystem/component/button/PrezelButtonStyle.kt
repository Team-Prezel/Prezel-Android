package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.number.PrezelShapes
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

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

@Immutable
data class PrezelButtonStyle(
    val buttonType: PrezelButtonType = PrezelButtonType.FILLED,
    val buttonHierarchy: PrezelButtonHierarchy = PrezelButtonHierarchy.PRIMARY,
    val buttonSize: PrezelButtonSize = PrezelButtonSize.REGULAR,
    val isRounded: Boolean = false,
)

@Composable
internal fun PrezelButtonIcon(
    icon: IconSource?,
    size: PrezelButtonSize,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    if (icon == null) return
    Icon(
        painter = icon.painter(),
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
internal fun prezelButtonShape(
    isRounded: Boolean,
    shapes: PrezelShapes = PrezelTheme.shapes,
): Shape = if (isRounded) shapes.V1000 else shapes.V4

@Composable
internal fun prezelButtonBorderStroke(
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
internal fun prezelButtonTextStyle(size: PrezelButtonSize): TextStyle =
    when (size) {
        PrezelButtonSize.XSMALL -> PrezelTheme.typography.caption2Medium
        PrezelButtonSize.SMALL -> PrezelTheme.typography.body3Medium
        PrezelButtonSize.REGULAR -> PrezelTheme.typography.body2Bold
    }

@Composable
internal fun prezelButtonContainerColor(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    colors: PrezelColors = PrezelTheme.colors,
): Color =
    when (type) {
        PrezelButtonType.FILLED -> {
            if (!enabled || hierarchy == PrezelButtonHierarchy.SECONDARY) {
                colors.bgLarge
            } else {
                colors.interactiveRegular
            }
        }

        PrezelButtonType.OUTLINED -> Color.Transparent
        PrezelButtonType.GHOST -> Color.Transparent
    }

@Composable
internal fun prezelButtonContentColor(
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
internal fun prezelButtonContentPadding(
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
