package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

val LocalPrezelButtonContent = PrezelButtonContentCompositionLocal()

interface PrezelButtonContentLoader {
    @Composable
    fun getContentColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color

    @Composable
    fun getBackgroundColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color

    @Composable
    fun getShape(
        isRounded: Boolean,
        isIconOnly: Boolean,
        size: ButtonSize,
    ): RoundedCornerShape
}

class PrezelButtonContentLoaderImpl : PrezelButtonContentLoader {
    @Composable
    override fun getContentColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color {
        if (!enabled) return PrezelTheme.colors.textDisabled
        if (hierarchy == ButtonHierarchy.SECONDARY) return PrezelTheme.colors.textMedium

        return when (type) {
            ButtonType.FILLED -> PrezelColorScheme.Dark.textLarge
            ButtonType.OUTLINED -> PrezelTheme.colors.interactiveRegular
            ButtonType.GHOST -> PrezelTheme.colors.interactiveRegular
        }
    }

    @Composable
    override fun getBackgroundColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
        enabled: Boolean,
    ): Color =
        when (type) {
            ButtonType.OUTLINED,
            ButtonType.GHOST,
            -> Color.Transparent

            ButtonType.FILLED -> {
                if (!enabled) {
                    PrezelTheme.colors.bgLarge
                } else if (hierarchy == ButtonHierarchy.SECONDARY) {
                    PrezelTheme.colors.bgLarge
                } else {
                    PrezelTheme.colors.interactiveRegular
                }
            }
        }

    @Composable
    override fun getShape(
        isRounded: Boolean,
        isIconOnly: Boolean,
        size: ButtonSize,
    ): RoundedCornerShape {
        if (isRounded) return PrezelTheme.shapes.V1000

        return when (size) {
            ButtonSize.REGULAR -> PrezelTheme.shapes.V8
            ButtonSize.SMALL -> if (isIconOnly) PrezelTheme.shapes.V6 else PrezelTheme.shapes.V4
            ButtonSize.XSMALL -> PrezelTheme.shapes.V4
        }
    }
}

@JvmInline
value class PrezelButtonContentCompositionLocal internal constructor(
    private val delegate: ProvidableCompositionLocal<PrezelButtonContentLoader> = staticCompositionLocalOf { PrezelButtonContentLoaderImpl() },
) {
    val current: PrezelButtonContentLoader
        @Composable get() = delegate.current

    infix fun provides(value: PrezelButtonContentLoader) = delegate provides value
}
