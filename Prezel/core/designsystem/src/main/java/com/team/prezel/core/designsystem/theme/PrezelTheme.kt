package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalPrezelColors = staticCompositionLocalOf<PrezelColors> { error("No PrezelColors provided") }

object PrezelTheme {
    val colors: PrezelColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelColors.current
}

@Composable
fun PrezelTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) PrezelColorScheme.Dark else PrezelColorScheme.Light

    CompositionLocalProvider(
        LocalPrezelColors provides colorScheme,
        content = content,
    )
}
