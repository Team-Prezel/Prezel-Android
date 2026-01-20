package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography

private val LocalPrezelColors = staticCompositionLocalOf<PrezelColors> { error("No PrezelColors provided") }
private val LocalPrezelTypography = staticCompositionLocalOf<PrezelTypography> { error("No PrezelTypography provided") }

object PrezelTheme {
    val colors: PrezelColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelColors.current

    val typography: PrezelTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelTypography.current
}

@Composable
fun PrezelTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) PrezelColorScheme.Dark else PrezelColorScheme.Light
    val typographyScheme = PrezelTypographyScheme.Default()

    CompositionLocalProvider(
        LocalPrezelColors provides colorScheme,
        LocalPrezelTypography provides typographyScheme,
        content = content,
    )
}
