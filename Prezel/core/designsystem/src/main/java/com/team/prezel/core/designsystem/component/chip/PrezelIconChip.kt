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
fun PrezelIconChip(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    type: PrezelChipType = PrezelChipType.FILLED,
    size: PrezelChipSize = PrezelChipSize.REGULAR,
    interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
    feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
    config: PrezelChipDefault = PrezelChipDefaults.getDefault(
        iconOnly = true,
        type = type,
        size = size,
        interaction = interaction,
        feedback = feedback,
    ),
) {
    PrezelChipLayout(
        modifier = modifier,
        text = null,
        iconResId = iconResId,
        config = config,
    )
}

private class PrezelIconChipTypeProvider : PreviewParameterProvider<PrezelChipType> {
    override val values: Sequence<PrezelChipType> = PrezelChipType.entries.asSequence()
}

@BasicPreview
@Composable
private fun PrezelChipPreview(
    @PreviewParameter(PrezelIconChipTypeProvider::class) type: PrezelChipType,
) {
    PreviewSection(title = "Chip - $type") {
        Text(text = "Size", style = PrezelTheme.typography.title2Medium, color = PrezelTheme.colors.textLarge)
        PreviewValueRow(name = "Regular") {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                type = type,
                size = PrezelChipSize.REGULAR,
            )
        }
        PreviewValueRow(name = "Small") {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                type = type,
                size = PrezelChipSize.SMALL,
            )
        }

        Text(text = "Interaction", style = PrezelTheme.typography.title2Medium, color = PrezelTheme.colors.textLarge)
        PreviewValueRow(name = "Default") {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                type = type,
                interaction = PrezelChipInteraction.DEFAULT,
            )
        }
        PreviewValueRow(name = "Active") {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                type = type,
                interaction = PrezelChipInteraction.ACTIVE,
            )
        }
        PreviewValueRow(name = "Disabled") {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                type = type,
                interaction = PrezelChipInteraction.DISABLED,
            )
        }

        Text(text = "Feedback", style = PrezelTheme.typography.title2Medium, color = PrezelTheme.colors.textLarge)
        PreviewValueRow(name = "Default") {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                type = type,
                feedback = PrezelChipFeedback.DEFAULT,
            )
        }
        PreviewValueRow(name = "Bad") {
            PrezelIconChip(
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
    @PreviewParameter(PrezelIconChipTypeProvider::class) type: PrezelChipType,
) {
    PreviewSection(title = "Custom Chip - $type") {
        PreviewRow {
            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = true,
                    type = type,
                    containerColor = PrezelTheme.colors.accentPurpleSmall,
                    iconColor = PrezelTheme.colors.accentPurpleRegular,
                    textColor = PrezelTheme.colors.accentPurpleRegular,
                    borderColor = PrezelTheme.colors.accentPurpleRegular,
                ),
            )

            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = true,
                    type = type,
                    containerColor = PrezelTheme.colors.accentTealSmall,
                    iconColor = PrezelTheme.colors.accentTealRegular,
                    textColor = PrezelTheme.colors.accentTealRegular,
                    borderColor = PrezelTheme.colors.accentTealRegular,
                ),
            )

            PrezelIconChip(
                iconResId = PrezelIcons.Blank,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = true,
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
