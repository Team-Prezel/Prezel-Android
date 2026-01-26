package com.team.prezel.core.designsystem.component.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

internal typealias PrezelChipPreviewContent = @Composable (PrezelChipStyle) -> Unit

private val DefaultInteractionVariants = persistentListOf(
    PrezelChipInteraction.DEFAULT,
    PrezelChipInteraction.ACTIVE,
    PrezelChipInteraction.DISABLED,
)

private val PreviewSizes = persistentListOf(
    PrezelChipSize.SMALL,
    PrezelChipSize.REGULAR,
)

@Composable
internal fun PrezelChipPreviewByType(
    type: PrezelChipType,
    content: PrezelChipPreviewContent,
) {
    Text(
        text = type.name,
        style = PrezelTheme.typography.title2Medium,
    )

    HorizontalDivider()

    PrezelChipBadSection(
        type = type,
        content = content,
    )

    HorizontalDivider()

    PrezelChipDefaultSection(
        type = type,
        content = content,
    )
}

@Composable
private fun PrezelChipBadSection(
    type: PrezelChipType,
    content: PrezelChipPreviewContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Feedback: BAD",
            style = PrezelTheme.typography.body2Medium,
        )

        PrezelChipPreviewBlock(
            type = type,
            feedback = PrezelChipFeedback.BAD,
            interaction = PrezelChipInteraction.DEFAULT,
            content = content,
        )
    }
}

@Composable
private fun PrezelChipDefaultSection(
    type: PrezelChipType,
    content: PrezelChipPreviewContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Feedback: DEFAULT",
            style = PrezelTheme.typography.body2Medium,
        )

        DefaultInteractionVariants.forEach { interaction ->
            Text(
                text = "Interaction: $interaction",
                style = PrezelTheme.typography.body3Medium,
            )

            PrezelChipPreviewBlock(
                type = type,
                feedback = PrezelChipFeedback.DEFAULT,
                interaction = interaction,
                content = content,
            )
        }
    }
}

@Composable
private fun PrezelChipPreviewBlock(
    type: PrezelChipType,
    interaction: PrezelChipInteraction,
    feedback: PrezelChipFeedback,
    content: PrezelChipPreviewContent,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PreviewSizes.forEach { size ->
            content(
                PrezelChipStyle(
                    type = type,
                    size = size,
                    interaction = interaction,
                    feedback = feedback,
                ),
            )
        }
    }
}
