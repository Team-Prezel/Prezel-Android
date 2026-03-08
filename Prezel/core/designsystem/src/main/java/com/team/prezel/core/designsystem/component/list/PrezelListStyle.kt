package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
enum class PrezelListSize {
    SMALL,
    REGULAR,
}

@Composable
internal fun prezelListContentPadding(
    size: PrezelListSize,
    nested: Boolean,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues {
    if (nested) {
        return PaddingValues(spacing.V0)
    }

    val vertical = when (size) {
        PrezelListSize.SMALL -> spacing.V10
        PrezelListSize.REGULAR -> spacing.V14
    }

    return PaddingValues(
        horizontal = spacing.V12,
        vertical = vertical,
    )
}

@Composable
internal fun prezelListTextStyle(size: PrezelListSize): TextStyle =
    when (size) {
        PrezelListSize.SMALL -> PrezelTheme.typography.body3Medium
        PrezelListSize.REGULAR -> PrezelTheme.typography.body2Medium
    }

@Composable
internal fun prezelListIconTextSpacing(
    size: PrezelListSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): Dp =
    when (size) {
        PrezelListSize.SMALL -> spacing.V8
        PrezelListSize.REGULAR -> spacing.V12
    }

@Composable
internal fun prezelListTextTrailingSpacing(
    size: PrezelListSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): Dp =
    when (size) {
        PrezelListSize.SMALL -> spacing.V6
        PrezelListSize.REGULAR -> spacing.V12
    }

@Composable
internal fun prezelListTrailingIconSpacing(
    size: PrezelListSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): Dp =
    when (size) {
        PrezelListSize.SMALL -> spacing.V6
        PrezelListSize.REGULAR -> spacing.V8
    }
