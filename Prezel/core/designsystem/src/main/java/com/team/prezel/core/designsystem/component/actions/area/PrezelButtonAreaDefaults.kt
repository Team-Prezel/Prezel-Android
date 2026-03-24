package com.team.prezel.core.designsystem.component.actions.area

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class PrezelButtonAreaDefault(
    val backgroundColor: Color,
    val borderColor: Color,
    val contentPadding: PaddingValues,
)

object PrezelButtonAreaDefaults {
    @Composable
    fun getDefault(
        showBackground: Boolean = false,
        isNested: Boolean = false,
        backgroundColor: Color = getBackgroundColor(showBackground = showBackground),
        borderColor: Color = getBorderColor(showBackground = showBackground),
        contentPadding: PaddingValues = getContentPadding(isNested = isNested),
    ): PrezelButtonAreaDefault =
        PrezelButtonAreaDefault(
            backgroundColor = backgroundColor,
            borderColor = borderColor,
            contentPadding = contentPadding,
        )

    @Composable
    private fun getBackgroundColor(showBackground: Boolean): Color = if (showBackground) PrezelTheme.colors.bgRegular else Color.Transparent

    @Composable
    private fun getBorderColor(showBackground: Boolean): Color = if (showBackground) PrezelTheme.colors.borderRegular else Color.Transparent

    @Composable
    private fun getContentPadding(isNested: Boolean): PaddingValues = PaddingValues(if (isNested) PrezelTheme.spacing.V0 else PrezelTheme.spacing.V20)
}
