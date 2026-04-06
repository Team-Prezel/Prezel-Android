package com.team.prezel.core.designsystem.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.number.PrezelShapes
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography
import com.team.prezel.core.designsystem.theme.PrezelTheme

internal val LocalPrezelChipColors = staticCompositionLocalOf { PrezelChipColors() }

enum class PrezelChipType {
    FILLED,
    OUTLINED,
}

enum class PrezelChipInteraction {
    DEFAULT,
    ACTIVE,
    DISABLED,
}

enum class PrezelChipFeedback {
    DEFAULT,
    BAD,
}

enum class PrezelChipSize {
    SMALL,
    REGULAR,
}

@Immutable
data class PrezelChipColors(
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
)

@Immutable
data class PrezelChipStyle(
    val type: PrezelChipType = PrezelChipType.FILLED,
    val size: PrezelChipSize = PrezelChipSize.REGULAR,
    val interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
    val feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
) {
    @Composable
    internal fun shape(shapes: PrezelShapes = PrezelTheme.shapes): Shape =
        when (size) {
            PrezelChipSize.SMALL -> shapes.V4
            PrezelChipSize.REGULAR -> shapes.V8
        }

    @Composable
    internal fun borderStroke(
        colors: PrezelColors = PrezelTheme.colors,
        stroke: PrezelStroke = PrezelTheme.stroke,
        chipColors: PrezelChipColors = LocalPrezelChipColors.current,
    ): BorderStroke? {
        if (type == PrezelChipType.FILLED) return null

        val borderColor =
            when {
                chipColors.contentColor != Color.Unspecified -> chipColors.contentColor
                feedback == PrezelChipFeedback.BAD -> colors.feedbackBadRegular
                interaction == PrezelChipInteraction.ACTIVE -> colors.interactiveRegular
                interaction == PrezelChipInteraction.DISABLED -> colors.borderRegular
                else -> colors.borderMedium
            }

        return BorderStroke(
            width = stroke.V1,
            color = borderColor,
        )
    }

    @Composable
    internal fun textStyle(typography: PrezelTypography = PrezelTheme.typography): TextStyle =
        when (size) {
            PrezelChipSize.SMALL -> typography.caption2Regular
            PrezelChipSize.REGULAR -> typography.caption1Regular
        }

    @Composable
    internal fun containerColor(
        iconOnly: Boolean,
        colors: PrezelColors = PrezelTheme.colors,
        chipColors: PrezelChipColors = LocalPrezelChipColors.current,
    ): Color {
        if (type == PrezelChipType.OUTLINED && iconOnly) {
            return Color.Transparent
        }

        return when {
            chipColors.containerColor != Color.Unspecified -> chipColors.containerColor
            feedback == PrezelChipFeedback.BAD -> colors.feedbackBadSmall
            interaction == PrezelChipInteraction.ACTIVE -> colors.interactiveXSmall
            interaction == PrezelChipInteraction.DISABLED -> colors.bgDisabled
            else -> {
                when (type) {
                    PrezelChipType.FILLED -> colors.bgLarge
                    PrezelChipType.OUTLINED -> colors.bgRegular
                }
            }
        }
    }

    @Composable
    internal fun contentColor(
        colors: PrezelColors = PrezelTheme.colors,
        chipColors: PrezelChipColors = LocalPrezelChipColors.current,
    ): Color =
        when {
            chipColors.contentColor != Color.Unspecified -> chipColors.contentColor
            feedback == PrezelChipFeedback.BAD -> colors.feedbackBadRegular
            interaction == PrezelChipInteraction.ACTIVE -> colors.interactiveRegular
            interaction == PrezelChipInteraction.DISABLED -> colors.iconDisabled
            else -> {
                when (type) {
                    PrezelChipType.FILLED -> colors.iconMedium
                    PrezelChipType.OUTLINED -> colors.iconRegular
                }
            }
        }

    @Composable
    internal fun contentPadding(
        iconOnly: Boolean,
        spacing: PrezelSpacing = PrezelTheme.spacing,
    ): PaddingValues {
        if (iconOnly) {
            val all = when (size) {
                PrezelChipSize.SMALL -> spacing.V6
                PrezelChipSize.REGULAR -> spacing.V8
            }
            return PaddingValues(all = all)
        }

        val horizontal = when (size) {
            PrezelChipSize.SMALL -> spacing.V6
            PrezelChipSize.REGULAR -> spacing.V8
        }

        val vertical = when (size) {
            PrezelChipSize.SMALL -> spacing.V4
            PrezelChipSize.REGULAR -> spacing.V6
        }

        return PaddingValues(horizontal = horizontal, vertical = vertical)
    }

    @Composable
    internal fun iconTextSpacing(spacing: PrezelSpacing = PrezelTheme.spacing): Dp =
        when (size) {
            PrezelChipSize.REGULAR -> spacing.V4
            PrezelChipSize.SMALL -> spacing.V2
        }

    internal fun iconSize(): Dp =
        when (size) {
            PrezelChipSize.SMALL -> 14.dp
            PrezelChipSize.REGULAR -> 16.dp
        }
}
