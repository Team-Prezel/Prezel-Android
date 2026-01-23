package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.dropShadowCache

enum class PrezelFloatingButtonHierarchy {
    PRIMARY,
    SECONDARY,
}

enum class PrezelFloatingButtonSize {
    SMALL,
    REGULAR,
}

@Immutable
data class PrezelFloatingButtonStyle(
    val hierarchy: PrezelFloatingButtonHierarchy = PrezelFloatingButtonHierarchy.PRIMARY,
    val size: PrezelFloatingButtonSize = PrezelFloatingButtonSize.REGULAR,
)

internal fun prezelFloatingButtonSize(size: PrezelFloatingButtonSize): Dp =
    when (size) {
        PrezelFloatingButtonSize.SMALL -> 36.dp
        PrezelFloatingButtonSize.REGULAR -> 48.dp
    }

internal fun prezelFloatingButtonIconSize(size: PrezelFloatingButtonSize): Dp =
    when (size) {
        PrezelFloatingButtonSize.SMALL -> 16.dp
        PrezelFloatingButtonSize.REGULAR -> 20.dp
    }

@Composable
internal fun Modifier.applyPrezelFloatingButtonShadow(): Modifier =
    this.dropShadowCache(
        color = Color(0x1F000713),
        shape = PrezelTheme.shapes.V1000,
        offsetY = 2.dp,
        blurRadius = 4.dp,
    )

@Composable
internal fun prezelFloatingButtonContentColor(
    hierarchy: PrezelFloatingButtonHierarchy,
    colors: PrezelColors = PrezelTheme.colors,
): Color =
    when (hierarchy) {
        PrezelFloatingButtonHierarchy.PRIMARY -> colors.solidWhite
        PrezelFloatingButtonHierarchy.SECONDARY -> colors.iconLarge
    }

@Composable
internal fun prezelFloatingButtonContainerColor(
    hierarchy: PrezelFloatingButtonHierarchy,
    colors: PrezelColors = PrezelTheme.colors,
): Color =
    when (hierarchy) {
        PrezelFloatingButtonHierarchy.PRIMARY -> colors.interactiveRegular
        PrezelFloatingButtonHierarchy.SECONDARY -> colors.bgLarge
    }
