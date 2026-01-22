package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.number.PrezelRadius
import com.team.prezel.core.designsystem.foundation.number.PrezelShapes
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography

private val LocalPrezelColors = staticCompositionLocalOf<PrezelColors> { error("No PrezelColors provided") }
private val LocalPrezelTypography = staticCompositionLocalOf<PrezelTypography> { error("No PrezelTypography provided") }
private val LocalPrezelRadius = staticCompositionLocalOf<PrezelRadius> { error("No PrezelRadius provided") }
private val LocalPrezelShapes = staticCompositionLocalOf<PrezelShapes> { error("No PrezelShapes provided") }
private val LocalPrezelSpacing = staticCompositionLocalOf<PrezelSpacing> { error("No PrezelSpacing provided") }
private val LocalPrezelStroke = staticCompositionLocalOf<PrezelStroke> { error("No PrezelStroke provided") }

object PrezelTheme {
    val colors: PrezelColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelColors.current

    val typography: PrezelTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelTypography.current

    val radius: PrezelRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelRadius.current

    val shapes: PrezelShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelShapes.current

    val spacing: PrezelSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelSpacing.current

    val stroke: PrezelStroke
        @Composable
        @ReadOnlyComposable
        get() = LocalPrezelStroke.current
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
        LocalPrezelRadius provides PrezelRadius,
        LocalPrezelShapes provides PrezelShapes,
        LocalPrezelSpacing provides PrezelSpacing,
        LocalPrezelStroke provides PrezelStroke,
        LocalTextStyle provides typographyScheme.body3Regular,
        LocalContentColor provides colorScheme.textLarge,
        content = content,
    )
}
