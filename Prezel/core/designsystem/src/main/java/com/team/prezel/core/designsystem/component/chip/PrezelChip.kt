package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipColors
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefault
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefaults
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipFeedback
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipInteraction
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipSize
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipType
import com.team.prezel.core.designsystem.component.chip.config.withCustomColors
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes iconResId: Int? = null,
    type: PrezelChipType = PrezelChipType.FILLED,
    size: PrezelChipSize = PrezelChipSize.REGULAR,
    interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
    feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
    config: PrezelChipDefault = PrezelChipDefaults.getDefault(
        iconOnly = text == null && iconResId != null,
        type = type,
        size = size,
        interaction = interaction,
        feedback = feedback,
    ),
    customColors: PrezelChipColors? = null,
) {
    require(text != null || iconResId != null) {
        "Chip은 text 또는 icon 중 하나는 반드시 필요합니다."
    }

    PrezelChipContent(
        modifier = modifier,
        text = text,
        iconResId = iconResId,
        config = config.withCustomColors(customColors ?: PrezelChipColors()),
    )
}

@Composable
private fun PrezelChipContent(
    modifier: Modifier = Modifier,
    text: String?,
    @DrawableRes iconResId: Int?,
    config: PrezelChipDefault,
) {
    val hasText = text != null
    val hasIcon = iconResId != null

    Surface(
        modifier = modifier,
        shape = config.shape,
        color = config.containerColor,
        border = config.borderStroke,
    ) {
        Row(
            modifier = Modifier.padding(config.contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            iconResId?.let { resId ->
                PrezelChipIcon(
                    iconResId = resId,
                    iconSize = config.iconSize,
                    tint = config.iconColor,
                )
            }

            if (hasText) {
                if (hasIcon) {
                    Spacer(modifier = Modifier.width(config.iconTextSpacing))
                }
                Text(
                    text = text,
                    color = config.textColor,
                    style = config.textStyle,
                )
            }
        }
    }
}

@Composable
private fun PrezelChipIcon(
    @DrawableRes iconResId: Int,
    iconSize: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = modifier.size(iconSize),
        tint = tint,
    )
}

private class PrezelChipTypeProvider : PreviewParameterProvider<PrezelChipType> {
    override val values: Sequence<PrezelChipType> = PrezelChipType.entries.asSequence()
}

@BasicPreview
@Composable
private fun PrezelChipPreview(
    @PreviewParameter(PrezelChipTypeProvider::class) type: PrezelChipType,
) {
    PreviewSection(title = "Chip - $type") {
        Text(text = "Size", style = PrezelTheme.typography.title2Medium, color = PrezelTheme.colors.textLarge)
        PreviewValueRow(name = "Regular") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                size = PrezelChipSize.REGULAR,
            )
        }
        PreviewValueRow(name = "Small") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                size = PrezelChipSize.SMALL,
            )
        }

        Text(text = "Interaction", style = PrezelTheme.typography.title2Medium, color = PrezelTheme.colors.textLarge)
        PreviewValueRow(name = "Default") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                interaction = PrezelChipInteraction.DEFAULT,
            )
        }
        PreviewValueRow(name = "Active") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                interaction = PrezelChipInteraction.ACTIVE,
            )
        }
        PreviewValueRow(name = "Disabled") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                interaction = PrezelChipInteraction.DISABLED,
            )
        }

        Text(text = "Feedback", style = PrezelTheme.typography.title2Medium, color = PrezelTheme.colors.textLarge)
        PreviewValueRow(name = "Default") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                feedback = PrezelChipFeedback.DEFAULT,
            )
        }
        PreviewValueRow(name = "Bad") {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                type = type,
                feedback = PrezelChipFeedback.BAD,
            )
        }
    }
}
