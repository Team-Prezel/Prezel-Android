package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class MenuItemSize {
    SMALL,
    REGULAR,
}

@Immutable
data class PrezelMenuItemDefault(
    val iconSize: Dp,
    val contentPadding: PaddingValues,
    val spacing: Dp,
    val textStyle: TextStyle,
    val contentColor: Color,
    val shape: RoundedCornerShape,
)

object PrezelMenuItemDefaults {
    @Composable
    fun getDefault(size: MenuItemSize): PrezelMenuItemDefault =
        PrezelMenuItemDefault(
            iconSize = getIconSize(size),
            contentPadding = getContentPadding(size),
            spacing = getSpacing(size),
            textStyle = getTextStyle(size),
            contentColor = getContentColor(),
            shape = getShape(),
        )

    private fun getIconSize(size: MenuItemSize): Dp =
        when (size) {
            MenuItemSize.SMALL -> 16.dp
            MenuItemSize.REGULAR -> 20.dp
        }

    @Composable
    private fun getContentPadding(size: MenuItemSize): PaddingValues {
        val verticalPadding = when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V4
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V8
        }

        val startPadding = when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V8
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V12
        }

        val endPadding = when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V10
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V16
        }

        return PaddingValues(
            start = startPadding,
            end = endPadding,
            top = verticalPadding,
            bottom = verticalPadding,
        )
    }

    @Composable
    private fun getSpacing(size: MenuItemSize): Dp =
        when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V4
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V8
        }

    @Composable
    private fun getTextStyle(size: MenuItemSize): TextStyle =
        when (size) {
            MenuItemSize.SMALL -> PrezelTheme.typography.body3Regular
            MenuItemSize.REGULAR -> PrezelTheme.typography.body2Regular
        }

    @Composable
    private fun getContentColor(): Color = PrezelTheme.colors.textMedium

    @Composable
    private fun getShape(): RoundedCornerShape = PrezelTheme.shapes.V8
}
