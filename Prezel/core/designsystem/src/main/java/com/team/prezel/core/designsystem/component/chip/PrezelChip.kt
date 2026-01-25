package com.team.prezel.core.designsystem.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: IconSource? = null,
    style: PrezelChipStyle = PrezelChipStyle(),
) {
    val hasText = text != null
    val hasIcon = icon != null
    val iconOnly = hasIcon && !hasText
    require(hasText || hasIcon) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }
    val (chipType, chipSize, interaction, feedback) = style

    Surface(
        modifier = modifier,
        shape = prezelChipShape(chipSize),
        color = prezelChipContainerColor(type = chipType, interaction = interaction, feedback = feedback, iconOnly = iconOnly),
        border = prezelChipBorderStroke(type = chipType, interaction = interaction, feedback = feedback),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides prezelChipTextStyle(chipSize),
            LocalContentColor provides prezelChipContentColor(interaction = interaction, feedback = feedback),
        ) {
            Row(
                modifier = Modifier.padding(prezelChipContentPadding(size = chipSize, onlyIcon = hasIcon && !hasText)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PrezelChipIcon(icon = icon, size = chipSize)

                if (!hasText) return@Row
                if (hasIcon) {
                    val spacing = if (chipSize == PrezelChipSize.REGULAR) PrezelTheme.spacing.V4 else PrezelTheme.spacing.V2
                    Spacer(modifier = Modifier.width(width = spacing))
                }

                Text(text = text)
            }
        }
    }
}

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: IconSource? = null,
    style: PrezelChipStyle = PrezelChipStyle(),
    containerColor: Color? = null,
    contentColor: Color? = null,
) {
    val hasText = text != null
    val hasIcon = icon != null
    val iconOnly = hasIcon && !hasText
    require(hasText || hasIcon) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }
    val (chipType, chipSize, interaction, feedback) = style

    val resolvedContainerColor =
        containerColor ?: prezelChipContainerColor(
            type = chipType,
            interaction = interaction,
            feedback = feedback,
            iconOnly = iconOnly,
        )

    val resolvedContentColor = contentColor ?: prezelChipContentColor(
        interaction = interaction,
        feedback = feedback,
    )

    val resolvedBorder =
        if (containerColor != null) {
            BorderStroke(0.dp, Color.Transparent)
        } else {
            prezelChipBorderStroke(
                type = chipType,
                interaction = interaction,
                feedback = feedback,
            )
        }

    Surface(
        modifier = modifier,
        shape = prezelChipShape(chipSize),
        color = resolvedContainerColor,
        border = resolvedBorder,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides prezelChipTextStyle(chipSize),
            LocalContentColor provides resolvedContentColor,
        ) {
            Row(
                modifier = Modifier.padding(prezelChipContentPadding(size = chipSize, onlyIcon = hasIcon && !hasText)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PrezelChipIcon(icon = icon, size = chipSize)

                if (!hasText) return@Row
                if (hasIcon) {
                    val spacing = if (chipSize == PrezelChipSize.REGULAR) PrezelTheme.spacing.V4 else PrezelTheme.spacing.V2
                    Spacer(modifier = Modifier.width(width = spacing))
                }

                Text(text = text)
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelChipPreview() {
    PrezelTheme {
        PrezelTheme {
            PreviewScaffold {
                PrezelChipPreviewByType(
                    type = PrezelChipType.FILLED,
                ) { style -> PrezelChipPreviewItem(style) }

                HorizontalDivider()

                PrezelChipPreviewByType(
                    type = PrezelChipType.OUTLINED,
                ) { style -> PrezelChipPreviewItem(style) }
            }
        }
    }
}

@Composable
private fun PrezelChipPreviewItem(style: PrezelChipStyle) {
    PrezelChip(
        text = "Label",
        icon = DrawableIcon(resId = PrezelIcons.Blank),
        style = style,
    )
}
