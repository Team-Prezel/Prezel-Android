package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class PrezelFloatingButtonMenuItemSize {
    SMALL,
    REGULAR,
    ;

    companion object {
        fun buttonMenuItemSize(size: PrezelFloatingButtonSize): PrezelFloatingButtonMenuItemSize =
            when (size) {
                PrezelFloatingButtonSize.SMALL -> SMALL
                PrezelFloatingButtonSize.REGULAR -> REGULAR
            }
    }
}

internal val LocalPrezelFloatingButtonMenuItemSize = compositionLocalOf { PrezelFloatingButtonMenuItemSize.REGULAR }

@Composable
internal fun prezelFloatingButtonMenuItemTextStyle(
    size: PrezelFloatingButtonMenuItemSize = LocalPrezelFloatingButtonMenuItemSize.current,
    typography: PrezelTypography = PrezelTheme.typography,
): TextStyle =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> typography.body3Regular
        PrezelFloatingButtonMenuItemSize.REGULAR -> typography.body2Regular
    }

@Composable
internal fun prezelFloatingButtonMenuItemPaddingValues(
    size: PrezelFloatingButtonMenuItemSize = LocalPrezelFloatingButtonMenuItemSize.current,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> spacing.V8 to spacing.V4
        PrezelFloatingButtonMenuItemSize.REGULAR -> spacing.V12 to spacing.V8
    }.let { (horizontal, vertical) -> PaddingValues(horizontal = horizontal, vertical = vertical) }

@Composable
internal fun prezelFloatingButtonMenuItemIconSize(size: PrezelFloatingButtonMenuItemSize = LocalPrezelFloatingButtonMenuItemSize.current): Dp =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> 16.dp
        PrezelFloatingButtonMenuItemSize.REGULAR -> 20.dp
    }

@Composable
internal fun prezelFloatingButtonMenuItemSpaceDp(
    size: PrezelFloatingButtonMenuItemSize = LocalPrezelFloatingButtonMenuItemSize.current,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): Dp =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> spacing.V4
        PrezelFloatingButtonMenuItemSize.REGULAR -> spacing.V8
    }
