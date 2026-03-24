package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class MenuSize {
    SMALL,
    REGULAR,
}

@Immutable
data class PrezelMenuDefault(
    val contentPadding: PaddingValues,
    val backgroundColor: Color,
    val verticalArrangement: Arrangement.Vertical,
    val shape: RoundedCornerShape,
)

object PrezelMenuDefaults {
    @Composable
    fun getDefault(size: MenuSize): PrezelMenuDefault =
        PrezelMenuDefault(
            contentPadding = getContentPadding(size),
            backgroundColor = getBackgroundColor(),
            verticalArrangement = getVerticalArrangement(),
            shape = getShape(),
        )

    @Composable
    private fun getContentPadding(size: MenuSize): PaddingValues =
        PaddingValues(
            when (size) {
                MenuSize.SMALL -> PrezelTheme.spacing.V4
                MenuSize.REGULAR -> PrezelTheme.spacing.V6
            },
        )

    @Composable
    private fun getBackgroundColor(): Color = PrezelTheme.colors.bgRegular

    @Composable
    private fun getVerticalArrangement(): Arrangement.Vertical = Arrangement.spacedBy(PrezelTheme.spacing.V4)

    @Composable
    private fun getShape(): RoundedCornerShape = PrezelTheme.shapes.V12
}
