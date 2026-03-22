package com.team.prezel.core.designsystem.component.actions.area

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class PrezelButtonAreaDefault(
    val mainButtonDefault: PrezelButtonDefault,
    val subButtonDefault: PrezelButtonDefault,
    val backgroundColor: Color,
    val contentPadding: PaddingValues,
)

object PrezelButtonAreaDefaults {
    @Composable
    fun getDefault(
        mainButtonDefault: PrezelButtonDefault = PrezelButtonDefaults.getDefault(isIconOnly = false),
        subButtonDefault: PrezelButtonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = false,
            hierarchy = ButtonHierarchy.SECONDARY,
        ),
        showBackground: Boolean = false,
        isNested: Boolean = false,
    ): PrezelButtonAreaDefault =
        PrezelButtonAreaDefault(
            mainButtonDefault = mainButtonDefault,
            subButtonDefault = subButtonDefault,
            backgroundColor = getBackgroundColor(showBackground = showBackground),
            contentPadding = getContentPadding(isNested = isNested),
        )

    @Composable
    private fun getBackgroundColor(showBackground: Boolean): Color = if (showBackground) PrezelTheme.colors.bgRegular else Color.Transparent

    @Composable
    private fun getContentPadding(isNested: Boolean): PaddingValues = PaddingValues(if (isNested) PrezelTheme.spacing.V0 else PrezelTheme.spacing.V20)
}
