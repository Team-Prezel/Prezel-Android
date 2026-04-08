package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefault
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefaults
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipFeedback
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipInteraction
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipLayout
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipSize
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewRow
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelChip(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    type: PrezelChipType = PrezelChipType.FILLED,
    size: PrezelChipSize = PrezelChipSize.REGULAR,
    interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
    feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
    config: PrezelChipDefault = PrezelChipDefaults.getDefault(
        iconOnly = false,
        type = type,
        size = size,
        interaction = interaction,
        feedback = feedback,
    ),
) {
    PrezelChipLayout(
        modifier = modifier,
        text = text,
        iconResId = iconResId,
        config = config,
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

@BasicPreview
@Composable
private fun PrezelChipCustomPreview(
    @PreviewParameter(PrezelChipTypeProvider::class) type: PrezelChipType,
) {
    PreviewSection(title = "Custom Chip - $type") {
        PreviewRow {
            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = false,
                    type = type,
                    containerColor = PrezelTheme.colors.accentPurpleSmall,
                    iconColor = PrezelTheme.colors.accentPurpleRegular,
                    textColor = PrezelTheme.colors.accentPurpleRegular,
                    borderColor = PrezelTheme.colors.accentPurpleRegular,
                ),
            )

            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = false,
                    type = type,
                    containerColor = PrezelTheme.colors.accentTealSmall,
                    iconColor = PrezelTheme.colors.accentTealRegular,
                    textColor = PrezelTheme.colors.accentTealRegular,
                    borderColor = PrezelTheme.colors.accentTealRegular,
                ),
            )

            PrezelChip(
                text = "Label",
                iconResId = PrezelIcons.Blank,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = false,
                    type = type,
                    containerColor = PrezelTheme.colors.accentMagentaSmall,
                    iconColor = PrezelTheme.colors.accentMagentaRegular,
                    textColor = PrezelTheme.colors.accentMagentaRegular,
                    borderColor = PrezelTheme.colors.accentMagentaRegular,
                ),
            )
        }
    }
}
