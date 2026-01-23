package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class PrezelFloatingButtonMenuItemSize {
    SMALL,
    REGULAR,
}

@Composable
internal fun prezelFloatingButtonMenuItemTextStyle(
    size: PrezelFloatingButtonMenuItemSize,
    typography: PrezelTypography = PrezelTheme.typography,
): TextStyle =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> typography.body3Regular
        PrezelFloatingButtonMenuItemSize.REGULAR -> typography.body2Regular
    }

@Composable
internal fun prezelFloatingButtonMenuItemPaddingValues(
    size: PrezelFloatingButtonMenuItemSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> spacing.V8 to spacing.V4
        PrezelFloatingButtonMenuItemSize.REGULAR -> spacing.V12 to spacing.V8
    }.let { (horizontal, vertical) -> PaddingValues(horizontal = horizontal, vertical = vertical) }

internal fun prezelFloatingButtonMenuItemIconSize(size: PrezelFloatingButtonMenuItemSize): Dp =
    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> 16.dp
        PrezelFloatingButtonMenuItemSize.REGULAR -> 20.dp
    }
