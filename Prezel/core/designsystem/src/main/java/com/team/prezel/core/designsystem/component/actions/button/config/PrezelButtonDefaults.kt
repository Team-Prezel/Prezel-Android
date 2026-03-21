package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class PrezelButtonDefault(
    val type: ButtonType,
    val size: ButtonSize,
    val hierarchy: ButtonHierarchy,
    val enabled: Boolean,
    val leadingIconTintColor: Color,
    val contentColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val shape: RoundedCornerShape,
    val textStyle: TextStyle,
)

object PrezelButtonDefaults {
    @Composable
    fun getDefault(
        isIconOnly: Boolean,
        type: ButtonType = ButtonType.FILLED,
        size: ButtonSize = ButtonSize.REGULAR,
        hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
        enabled: Boolean = true,
        isRounded: Boolean = false,
        leadingIconTintColor: Color = getContentColor(type = type, hierarchy = hierarchy, enabled = enabled),
        contentColor: Color = getContentColor(type = type, hierarchy = hierarchy, enabled = enabled),
        backgroundColor: Color = getBackgroundColor(type = type, hierarchy = hierarchy, enabled = enabled),
        borderColor: Color = getBorderColor(type = type, hierarchy = hierarchy, enabled = enabled),
        shape: RoundedCornerShape = getShape(isRounded = isRounded, isIconOnly = isIconOnly, size = size),
        textStyle: TextStyle = getTextStyle(size = size),
    ) = PrezelButtonDefault(
        type = type,
        size = size,
        hierarchy = hierarchy,
        enabled = enabled,
        leadingIconTintColor = leadingIconTintColor,
        contentColor = contentColor,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        shape = shape,
        textStyle = textStyle,
    )

    @Composable
    private fun getContentColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color = LocalPrezelButtonContent.current.getContentColor(type = type, hierarchy = hierarchy, enabled = enabled)

    @Composable
    private fun getBackgroundColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color = LocalPrezelButtonContent.current.getBackgroundColor(type = type, hierarchy = hierarchy, enabled = enabled)

    @Composable
    private fun getShape(
        isRounded: Boolean,
        isIconOnly: Boolean,
        size: ButtonSize,
    ): RoundedCornerShape = LocalPrezelButtonContent.current.getShape(isRounded = isRounded, isIconOnly = isIconOnly, size = size)

    @Composable
    private fun getBorderColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color = LocalPrezelButtonBorder.current.getBorderColor(type = type, hierarchy = hierarchy, enabled = enabled)

    @Composable
    private fun getTextStyle(size: ButtonSize): TextStyle = LocalPrezelButtonTextStyle.current.getTextStyle(size = size)
}
