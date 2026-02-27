package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
enum class PrezelListSize {
    SMALL,
    REGULAR,
}

@Composable
internal fun PrezelListIcon(
    icon: IconSource,
    size: PrezelListSize,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = icon.painter(),
        contentDescription = icon.contentDescription(),
        modifier = modifier.size(
            when (size) {
                PrezelListSize.SMALL -> 20.dp
                PrezelListSize.REGULAR -> 24.dp
            },
        ),
    )
}

@Composable
internal fun prezelListVerticalPadding(
    size: PrezelListSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): Dp =
    when (size) {
        PrezelListSize.SMALL -> spacing.V10
        PrezelListSize.REGULAR -> spacing.V14
    }

@Composable
internal fun prezelListTextStyle(
    size: PrezelListSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): TextStyle =
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
