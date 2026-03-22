package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class PrezelButtonDefault(
    val type: ButtonType,
    val size: ButtonSize,
    val hierarchy: ButtonHierarchy,
    val enabled: Boolean,
    val contentColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val shape: RoundedCornerShape,
    val textStyle: TextStyle,
    val contentPadding: PaddingValues,
    val iconSpacing: Dp,
    val iconSize: Dp,
) {
    val hasBorder: Boolean = borderWidth > 0.dp
}

object PrezelButtonDefaults {
    @Composable
    fun getDefault(
        isIconOnly: Boolean,
        type: ButtonType = ButtonType.FILLED,
        size: ButtonSize = ButtonSize.REGULAR,
        hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
        enabled: Boolean = true,
        isRounded: Boolean = false,
        contentColor: Color = getContentColor(type = type, hierarchy = hierarchy, enabled = enabled),
        backgroundColor: Color = getBackgroundColor(type = type, hierarchy = hierarchy, enabled = enabled),
        borderColor: Color = getBorderColor(type = type, hierarchy = hierarchy, enabled = enabled),
        borderWidth: Dp = getBorderWidth(type = type),
        shape: RoundedCornerShape = getShape(isRounded = isRounded, isIconOnly = isIconOnly, size = size),
        textStyle: TextStyle = getTextStyle(size = size),
        contentPadding: PaddingValues = getContentPadding(size = size, isIconOnly = isIconOnly),
        iconSpacing: Dp = getIconSpacing(size = size),
        iconSize: Dp = getIconSize(size = size),
    ) = PrezelButtonDefault(
        type = type,
        size = size,
        hierarchy = hierarchy,
        enabled = enabled,
        contentColor = contentColor,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        borderWidth = borderWidth,
        shape = shape,
        textStyle = textStyle,
        contentPadding = contentPadding,
        iconSpacing = iconSpacing,
        iconSize = iconSize,
    )

    @Composable
    private fun getContentColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color {
        if (!enabled) return PrezelTheme.colors.textDisabled
        if (hierarchy == ButtonHierarchy.SECONDARY) return PrezelTheme.colors.textMedium

        return when (type) {
            ButtonType.FILLED -> PrezelColorScheme.Dark.textLarge
            ButtonType.OUTLINED,
            ButtonType.GHOST,
            -> PrezelTheme.colors.interactiveRegular
        }
    }

    @Composable
    private fun getBackgroundColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color =
        when (type) {
            ButtonType.OUTLINED,
            ButtonType.GHOST,
            -> Color.Transparent

            ButtonType.FILLED -> {
                if (!enabled || hierarchy == ButtonHierarchy.SECONDARY) {
                    PrezelTheme.colors.bgLarge
                } else {
                    PrezelTheme.colors.interactiveRegular
                }
            }
        }

    @Composable
    private fun getBorderColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color {
        if (type != ButtonType.OUTLINED) return Color.Transparent
        if (!enabled) return PrezelTheme.colors.borderDisabled

        return when (hierarchy) {
            ButtonHierarchy.PRIMARY -> PrezelTheme.colors.interactiveRegular
            ButtonHierarchy.SECONDARY -> PrezelTheme.colors.borderMedium
        }
    }

    @Composable
    private fun getBorderWidth(type: ButtonType): Dp = if (type == ButtonType.OUTLINED) PrezelTheme.stroke.V1 else 0.dp

    @Composable
    private fun getShape(
        isRounded: Boolean,
        isIconOnly: Boolean,
        size: ButtonSize,
    ): RoundedCornerShape {
        if (isRounded) return PrezelTheme.shapes.V1000

        return when (size) {
            ButtonSize.REGULAR -> PrezelTheme.shapes.V8
            ButtonSize.SMALL -> if (isIconOnly) PrezelTheme.shapes.V6 else PrezelTheme.shapes.V4
            ButtonSize.XSMALL -> PrezelTheme.shapes.V4
        }
    }

    @Composable
    private fun getTextStyle(size: ButtonSize): TextStyle =
        when (size) {
            ButtonSize.XSMALL -> PrezelTheme.typography.caption2Medium
            ButtonSize.SMALL -> PrezelTheme.typography.body3Medium
            ButtonSize.REGULAR -> PrezelTheme.typography.body2Bold
        }

    @Composable
    private fun getContentPadding(
        size: ButtonSize,
        isIconOnly: Boolean,
    ): PaddingValues {
        if (isIconOnly) {
            val all = when (size) {
                ButtonSize.XSMALL -> PrezelTheme.spacing.V8
                ButtonSize.SMALL -> PrezelTheme.spacing.V10
                ButtonSize.REGULAR -> PrezelTheme.spacing.V14
            }

            return PaddingValues(all = all)
        }

        val horizontal = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V10
            ButtonSize.SMALL -> PrezelTheme.spacing.V12
            ButtonSize.REGULAR -> PrezelTheme.spacing.V16
        }
        val vertical = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V6
            ButtonSize.SMALL -> PrezelTheme.spacing.V8
            ButtonSize.REGULAR -> PrezelTheme.spacing.V12
        }

        return PaddingValues(horizontal = horizontal, vertical = vertical)
    }

    @Composable
    private fun getIconSpacing(size: ButtonSize): Dp =
        when (size) {
            ButtonSize.XSMALL,
            ButtonSize.SMALL,
            -> PrezelTheme.spacing.V4

            ButtonSize.REGULAR -> PrezelTheme.spacing.V8
        }

    private fun getIconSize(size: ButtonSize): Dp =
        when (size) {
            ButtonSize.XSMALL -> 14.dp
            ButtonSize.SMALL -> 16.dp
            ButtonSize.REGULAR -> 20.dp
        }
}
