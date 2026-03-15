package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.number.PrezelShapes
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
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
    val showUnderline: Boolean = false,
)

@Immutable
internal data class PrezelButtonAppearance(
    val textStyle: TextStyle,
    val contentColor: Color,
    val contentPadding: PaddingValues,
    val iconSpacing: Dp,
    val shape: Shape,
    val containerColor: Color,
    val borderStroke: BorderStroke,
    val iconSize: Dp,
) {
    companion object {
        @Composable
        fun of(
            style: PrezelButtonStyle,
            isIconOnly: Boolean,
            enabled: Boolean,
        ): PrezelButtonAppearance {
            val (buttonType, buttonHierarchy, buttonSize, isRounded) = style

            return PrezelButtonAppearance(
                textStyle = prezelButtonTextStyle(size = buttonSize),
                contentColor = prezelButtonContentColor(
                    type = buttonType,
                    hierarchy = buttonHierarchy,
                    enabled = enabled,
                ),
                contentPadding = prezelButtonContentPadding(
                    size = buttonSize,
                    isIconOnly = isIconOnly,
                ),
                iconSpacing = prezelButtonIconSpacing(size = buttonSize),
                shape = prezelButtonShape(
                    isIconOnly = isIconOnly,
                    isRounded = isRounded,
                    buttonSize = buttonSize,
                ),
                containerColor = prezelButtonContainerColor(
                    type = buttonType,
                    hierarchy = buttonHierarchy,
                    enabled = enabled,
                ),
                borderStroke = prezelButtonBorderStroke(
                    type = buttonType,
                    hierarchy = buttonHierarchy,
                    enabled = enabled,
                ),
                iconSize = prezelButtonIconSize(size = buttonSize),
            )
        }
    }
}

internal fun Modifier.applyButtonAppearance(appearance: PrezelButtonAppearance): Modifier =
    this
        .clip(appearance.shape)
        .background(appearance.containerColor)
        .border(
            border = appearance.borderStroke,
            shape = appearance.shape,
        )

@Composable
private fun prezelButtonIconSpacing(
    size: PrezelButtonSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): Dp =
    when (size) {
        PrezelButtonSize.XSMALL -> spacing.V4
        PrezelButtonSize.SMALL -> spacing.V4
        PrezelButtonSize.REGULAR -> spacing.V8
    }

@Composable
private fun prezelButtonShape(
    isIconOnly: Boolean,
    isRounded: Boolean,
    buttonSize: PrezelButtonSize,
    shapes: PrezelShapes = PrezelTheme.shapes,
): Shape =
    when (isRounded) {
        true -> shapes.V1000
        false -> {
            when (buttonSize) {
                PrezelButtonSize.REGULAR -> shapes.V8
                PrezelButtonSize.SMALL -> if (isIconOnly) shapes.V6 else shapes.V4
                PrezelButtonSize.XSMALL -> shapes.V4
            }
        }
    }

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
private fun prezelButtonContentColor(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    colors: PrezelColors = PrezelTheme.colors,
): Color {
    if (!enabled) return colors.textDisabled
    if (hierarchy == PrezelButtonHierarchy.SECONDARY) return colors.textMedium

    return when (type) {
        PrezelButtonType.FILLED -> colors.textLarge
        PrezelButtonType.OUTLINED -> colors.interactiveRegular
        PrezelButtonType.GHOST -> colors.interactiveRegular
    }
}

@Composable
private fun prezelButtonContentPadding(
    size: PrezelButtonSize,
    isIconOnly: Boolean,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues {
    if (isIconOnly) return prezelIconButtonContentPadding(size)

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

@Composable
private fun prezelIconButtonContentPadding(
    size: PrezelButtonSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues =
    PaddingValues(
        when (size) {
            PrezelButtonSize.XSMALL -> spacing.V8
            PrezelButtonSize.SMALL -> spacing.V10
            PrezelButtonSize.REGULAR -> spacing.V14
        },
    )

@Composable
private fun prezelButtonIconSize(size: PrezelButtonSize): Dp =
    when (size) {
        PrezelButtonSize.XSMALL -> 14.dp
        PrezelButtonSize.SMALL -> 16.dp
        PrezelButtonSize.REGULAR -> 20.dp
    }
