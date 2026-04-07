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
import com.team.prezel.core.designsystem.theme.PrezelTheme

internal val LocalPrezelChipColors = staticCompositionLocalOf { PrezelChipColors() }

internal val LocalPrezelChipTextColor = staticCompositionLocalOf<Color> {
    error("No PrezelChipTextColor provided")
}

internal val LocalPrezelChipIconColor = staticCompositionLocalOf<Color> {
    error("No PrezelChipIconColor provided")
}

@Immutable
data class PrezelChipColors(
    val containerColor: Color = Color.Unspecified,
    val iconColor: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
)

@Immutable
data class PrezelChipDefault(
    val shape: Shape,
    val borderStroke: BorderStroke?,
    val textStyle: TextStyle,
    val containerColor: Color,
    val iconColor: Color,
    val textColor: Color,
    val contentPadding: PaddingValues,
    val iconTextSpacing: Dp,
    val iconSize: Dp,
)

object PrezelChipDefaults {
    @Composable
    fun getDefault(
        iconOnly: Boolean,
        type: PrezelChipType = PrezelChipType.FILLED,
        size: PrezelChipSize = PrezelChipSize.REGULAR,
        interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
        feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
        chipColors: PrezelChipColors = LocalPrezelChipColors.current,
        shape: Shape = getShape(size = size),
        borderStroke: BorderStroke? = getBorderStroke(
            type = type,
            interaction = interaction,
            feedback = feedback,
            chipColors = chipColors,
        ),
        textStyle: TextStyle = getTextStyle(size = size),
        containerColor: Color = getContainerColor(
            iconOnly = iconOnly,
            type = type,
            interaction = interaction,
            feedback = feedback,
            chipColors = chipColors,
        ),
        iconColor: Color = getIconColor(
            type = type,
            interaction = interaction,
            feedback = feedback,
            chipColors = chipColors,
        ),
        textColor: Color = getTextColor(
            type = type,
            interaction = interaction,
            feedback = feedback,
            chipColors = chipColors,
        ),
        contentPadding: PaddingValues = getContentPadding(size = size, iconOnly = iconOnly),
        iconTextSpacing: Dp = getIconTextSpacing(size = size),
        iconSize: Dp = getIconSize(size = size),
    ) = PrezelChipDefault(
        shape = shape,
        borderStroke = borderStroke,
        textStyle = textStyle,
        containerColor = containerColor,
        iconColor = iconColor,
        textColor = textColor,
        contentPadding = contentPadding,
        iconTextSpacing = iconTextSpacing,
        iconSize = iconSize,
    )

    @Composable
    private fun getShape(size: PrezelChipSize): Shape =
        when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.shapes.V4
            PrezelChipSize.REGULAR -> PrezelTheme.shapes.V8
        }

    @Composable
    private fun getBorderStroke(
        type: PrezelChipType,
        interaction: PrezelChipInteraction,
        feedback: PrezelChipFeedback,
        chipColors: PrezelChipColors,
    ): BorderStroke? {
        if (type == PrezelChipType.FILLED) return null

        val borderColor =
            when {
                chipColors.iconColor != Color.Unspecified -> chipColors.iconColor
                feedback == PrezelChipFeedback.BAD -> PrezelTheme.colors.feedbackBadRegular
                interaction == PrezelChipInteraction.ACTIVE -> PrezelTheme.colors.interactiveRegular
                interaction == PrezelChipInteraction.DISABLED -> PrezelTheme.colors.borderRegular
                else -> PrezelTheme.colors.borderMedium
            }

        return BorderStroke(
            width = PrezelTheme.stroke.V1,
            color = borderColor,
        )
    }

    @Composable
    private fun getTextStyle(size: PrezelChipSize): TextStyle =
        when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.typography.caption2Regular
            PrezelChipSize.REGULAR -> PrezelTheme.typography.caption1Regular
        }

    @Composable
    private fun getContainerColor(
        iconOnly: Boolean,
        type: PrezelChipType,
        interaction: PrezelChipInteraction,
        feedback: PrezelChipFeedback,
        chipColors: PrezelChipColors,
    ): Color {
        if (type == PrezelChipType.OUTLINED && iconOnly) {
            return Color.Transparent
        }

        return when {
            chipColors.containerColor != Color.Unspecified -> chipColors.containerColor
            feedback == PrezelChipFeedback.BAD -> PrezelTheme.colors.feedbackBadSmall
            interaction == PrezelChipInteraction.ACTIVE -> PrezelTheme.colors.interactiveXSmall
            interaction == PrezelChipInteraction.DISABLED -> PrezelTheme.colors.bgDisabled
            else -> {
                when (type) {
                    PrezelChipType.FILLED -> PrezelTheme.colors.bgLarge
                    PrezelChipType.OUTLINED -> PrezelTheme.colors.bgRegular
                }
            }
        }
    }

    @Composable
    private fun getIconColor(
        type: PrezelChipType,
        interaction: PrezelChipInteraction,
        feedback: PrezelChipFeedback,
        chipColors: PrezelChipColors,
    ): Color =
        when {
            chipColors.iconColor != Color.Unspecified -> chipColors.iconColor
            feedback == PrezelChipFeedback.BAD -> PrezelTheme.colors.feedbackBadRegular
            interaction == PrezelChipInteraction.ACTIVE -> PrezelTheme.colors.interactiveRegular
            interaction == PrezelChipInteraction.DISABLED -> PrezelTheme.colors.iconDisabled
            else -> {
                when (type) {
                    PrezelChipType.FILLED -> PrezelTheme.colors.iconMedium
                    PrezelChipType.OUTLINED -> PrezelTheme.colors.iconRegular
                }
            }
        }

    @Composable
    private fun getTextColor(
        type: PrezelChipType,
        interaction: PrezelChipInteraction,
        feedback: PrezelChipFeedback,
        chipColors: PrezelChipColors,
    ): Color =
        when {
            chipColors.textColor != Color.Unspecified -> chipColors.textColor
            feedback == PrezelChipFeedback.BAD -> PrezelTheme.colors.feedbackBadRegular
            interaction == PrezelChipInteraction.ACTIVE -> PrezelTheme.colors.interactiveRegular
            interaction == PrezelChipInteraction.DISABLED -> PrezelTheme.colors.textDisabled
            else -> {
                when (type) {
                    PrezelChipType.FILLED -> PrezelTheme.colors.textMedium
                    PrezelChipType.OUTLINED -> PrezelTheme.colors.textRegular
                }
            }
        }

    @Composable
    private fun getContentPadding(
        size: PrezelChipSize,
        iconOnly: Boolean,
    ): PaddingValues {
        if (iconOnly) {
            val all = when (size) {
                PrezelChipSize.SMALL -> PrezelTheme.spacing.V6
                PrezelChipSize.REGULAR -> PrezelTheme.spacing.V8
            }
            return PaddingValues(all = all)
        }

        val horizontal = when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V6
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V8
        }

        val vertical = when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V4
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V6
        }

        return PaddingValues(horizontal = horizontal, vertical = vertical)
    }

    @Composable
    private fun getIconTextSpacing(size: PrezelChipSize): Dp =
        when (size) {
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V4
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V2
        }

    private fun getIconSize(size: PrezelChipSize): Dp =
        when (size) {
            PrezelChipSize.SMALL -> 14.dp
            PrezelChipSize.REGULAR -> 16.dp
        }
}
