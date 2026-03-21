package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.team.prezel.core.designsystem.theme.PrezelTheme

val LocalPrezelButtonTextStyle = PrezelButtonTextStyleCompositionLocal()

interface PrezelButtonTextStyleLoader {
    @Composable
    fun getTextStyle(size: ButtonSize): TextStyle
}

internal class PrezelButtonTextStyleLoaderImpl : PrezelButtonTextStyleLoader {
    @Composable
    override fun getTextStyle(size: ButtonSize): TextStyle =
        when (size) {
            ButtonSize.XSMALL -> PrezelTheme.typography.caption2Medium
            ButtonSize.SMALL -> PrezelTheme.typography.body3Medium
            ButtonSize.REGULAR -> PrezelTheme.typography.body2Bold
        }
}

@JvmInline
value class PrezelButtonTextStyleCompositionLocal internal constructor(
    private val delegate: ProvidableCompositionLocal<PrezelButtonTextStyleLoader> = staticCompositionLocalOf { PrezelButtonTextStyleLoaderImpl() },
) {
    val current: PrezelButtonTextStyleLoader
        @Composable get() = delegate.current

    infix fun provides(value: PrezelButtonTextStyleLoader) = delegate provides value
}
