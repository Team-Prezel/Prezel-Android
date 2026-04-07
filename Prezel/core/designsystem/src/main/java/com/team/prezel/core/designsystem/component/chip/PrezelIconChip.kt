package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview

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
    PrezelChip(
        modifier = modifier,
        iconResId = iconResId,
        type = type,
        size = size,
        interaction = interaction,
        feedback = feedback,
        config = config,
    )
}

@BasicPreview
@Composable
private fun PrezelChipSizePreview() {
    PrezelChipSizePreviewContent { type, size ->
        PrezelIconChip(
            iconResId = PrezelIcons.Blank,
            type = type,
            size = size,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelChipInteractionPreview() {
    PrezelChipInteractionPreviewContent { type, interaction ->
        PrezelIconChip(
            iconResId = PrezelIcons.Blank,
            type = type,
            interaction = interaction,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelChipFeedbackPreview() {
    PrezelChipFeedbackPreviewContent { type, feedback ->
        PrezelIconChip(
            iconResId = PrezelIcons.Blank,
            type = type,
            feedback = feedback,
        )
    }
}
