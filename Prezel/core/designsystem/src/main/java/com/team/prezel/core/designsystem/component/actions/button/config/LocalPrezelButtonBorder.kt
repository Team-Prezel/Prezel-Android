package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

val LocalPrezelButtonBorder = PrezelButtonBorderCompositionLocal()

interface PrezelButtonBorderLoader {
    @Composable
    fun getBorderColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color
}

internal class PrezelButtonBorderLoaderImpl : PrezelButtonBorderLoader {
    @Composable
    override fun getBorderColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color {
        if (type != ButtonType.OUTLINED) return Color.Transparent
        if (!enabled) return PrezelTheme.colors.borderDisabled

        return when (hierarchy) {
            ButtonHierarchy.PRIMARY -> PrezelTheme.colors.interactiveRegular
            ButtonHierarchy.SECONDARY -> PrezelTheme.colors.borderMedium
        }
    }
}

@JvmInline
value class PrezelButtonBorderCompositionLocal internal constructor(
    private val delegate: ProvidableCompositionLocal<PrezelButtonBorderLoader> = staticCompositionLocalOf { PrezelButtonBorderLoaderImpl() },
) {
    val current: PrezelButtonBorderLoader
        @Composable get() = delegate.current

    infix fun provides(value: PrezelButtonBorderLoader) = delegate provides value
}
