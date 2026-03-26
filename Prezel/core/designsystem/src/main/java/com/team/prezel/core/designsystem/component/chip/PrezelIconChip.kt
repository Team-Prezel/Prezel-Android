package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow

@Composable
fun PrezelIconChip(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    style: PrezelChipStyle = PrezelChipStyle(),
) {
    PrezelChip(
        modifier = modifier,
        iconResId = iconResId,
        style = style,
    )
}

@BasicPreview
@Composable
private fun PrezelChipSizePreview() {
    PreviewSection(
        title = "Chip / Size",
        description = "Chip의 크기를 조절합니다.",
    ) {
        PrezelChipType.entries.forEach { type ->
            PrezelChipSize.entries.forEach { size ->
                PreviewValueRow(
                    name = type.name,
                    valueLabel = size.name,
                ) {
                    PrezelIconChip(
                        iconResId = PrezelIcons.Blank,
                        style = PrezelChipStyle(type = type, size = size),
                    )
                }
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelChipInteractionPreview() {
    PreviewSection(
        title = "Chip / Interaction",
        description = "Chip의 상호작용 상태를 조절합니다.",
    ) {
        PrezelChipType.entries.forEach { type ->
            PrezelChipInteraction.entries.forEach { interaction ->
                PreviewValueRow(
                    name = type.name,
                    valueLabel = interaction.name,
                ) {
                    PrezelIconChip(
                        iconResId = PrezelIcons.Blank,
                        style = PrezelChipStyle(type = type, interaction = interaction),
                    )
                }
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelChipFeedbackPreview() {
    PreviewSection(
        title = "Chip / Feedback",
        description = "Chip의 피드백 상태를 조절합니다.",
    ) {
        PrezelChipType.entries.forEach { type ->
            PrezelChipFeedback.entries.forEach { feedback ->
                PreviewValueRow(
                    name = type.name,
                    valueLabel = feedback.name,
                ) {
                    PrezelIconChip(
                        iconResId = PrezelIcons.Blank,
                        style = PrezelChipStyle(type = type, feedback = feedback),
                    )
                }
            }
        }
    }
}
