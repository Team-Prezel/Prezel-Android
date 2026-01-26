package com.team.prezel.core.designsystem.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.foundation.number.PrezelShapes
import com.team.prezel.core.designsystem.foundation.number.PrezelSpacing
import com.team.prezel.core.designsystem.foundation.number.PrezelStroke
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.theme.PrezelTheme

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
data class PrezelChipStyle(
    val type: PrezelChipType = PrezelChipType.FILLED,
    val size: PrezelChipSize = PrezelChipSize.REGULAR,
    val interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
    val feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
)

@Composable
internal fun PrezelChipIcon(
    icon: IconSource?,
    size: PrezelChipSize,
    modifier: Modifier = Modifier,
) {
    if (icon == null) return

    Icon(
        painter = icon.painter(),
        contentDescription = icon.contentDescription(),
        modifier = modifier.size(
            when (size) {
                PrezelChipSize.SMALL -> 14.dp
                PrezelChipSize.REGULAR -> 16.dp
            },
        ),
    )
}

@Composable
internal fun prezelChipShape(
    size: PrezelChipSize,
    shapes: PrezelShapes = PrezelTheme.shapes,
): Shape =
    when (size) {
        PrezelChipSize.SMALL -> shapes.V4
        PrezelChipSize.REGULAR -> shapes.V8
    }

@Composable
internal fun prezelChipBorderStroke(
    type: PrezelChipType,
    interaction: PrezelChipInteraction,
    feedback: PrezelChipFeedback,
    colors: PrezelColors = PrezelTheme.colors,
    stroke: PrezelStroke = PrezelTheme.stroke,
): BorderStroke {
    if (type == PrezelChipType.FILLED) return BorderStroke(0.dp, Color.Transparent)

    val borderColor =
        when {
            feedback == PrezelChipFeedback.BAD -> {
                colors.feedbackBadRegular
            }

            interaction == PrezelChipInteraction.ACTIVE -> {
                colors.interactiveRegular
            }

            interaction == PrezelChipInteraction.DISABLED -> {
                colors.borderRegular
            }

            else -> {
                colors.borderMedium
            }
        }

    return BorderStroke(
        width = stroke.V1,
        color = borderColor,
    )
}

@Composable
internal fun prezelChipTextStyle(size: PrezelChipSize): TextStyle =
    when (size) {
        PrezelChipSize.SMALL -> PrezelTheme.typography.caption2Regular
        PrezelChipSize.REGULAR -> PrezelTheme.typography.caption1Regular
    }

@Composable
internal fun prezelChipContainerColor(
    type: PrezelChipType,
    interaction: PrezelChipInteraction,
    feedback: PrezelChipFeedback,
    iconOnly: Boolean,
    colors: PrezelColors = PrezelTheme.colors,
): Color {
    if (type == PrezelChipType.OUTLINED && iconOnly) {
        return Color.Transparent
    }

    return when {
        feedback == PrezelChipFeedback.BAD -> {
            colors.feedbackBadSmall
        }

        interaction == PrezelChipInteraction.ACTIVE -> {
            colors.interactiveXSmall
        }

        interaction == PrezelChipInteraction.DISABLED -> {
            colors.bgLarge
        }

        else -> {
            when (type) {
                PrezelChipType.FILLED -> colors.bgMedium
                PrezelChipType.OUTLINED -> colors.bgRegular
            }
        }
    }
}

@Composable
internal fun prezelChipContentColor(
    interaction: PrezelChipInteraction,
    feedback: PrezelChipFeedback,
    colors: PrezelColors = PrezelTheme.colors,
): Color =
    when {
        feedback == PrezelChipFeedback.BAD -> {
            colors.feedbackBadRegular
        }

        interaction == PrezelChipInteraction.ACTIVE -> {
            colors.interactiveRegular
        }

        interaction == PrezelChipInteraction.DISABLED -> {
            colors.iconDisabled
        }

        else -> {
            colors.iconRegular
        }
    }

@Composable
internal fun prezelChipContentPadding(
    size: PrezelChipSize,
    onlyIcon: Boolean = false,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues {
    if (onlyIcon) return prezelIconChipContentPadding(size)

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
private fun prezelIconChipContentPadding(
    size: PrezelChipSize,
    spacing: PrezelSpacing = PrezelTheme.spacing,
): PaddingValues =
    when (size) {
        PrezelChipSize.SMALL -> spacing.V6
        PrezelChipSize.REGULAR -> spacing.V8
    }.let { spacing -> PaddingValues(all = spacing) }
