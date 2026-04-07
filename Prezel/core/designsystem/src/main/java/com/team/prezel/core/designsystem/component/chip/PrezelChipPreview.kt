package com.team.prezel.core.designsystem.component.chip

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewRow
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
private fun <T> PrezelChipPreviewContent(
    previewTitle: String,
    previewDescription: String,
    values: List<T>,
    valueLabel: (T) -> String = { it.toString() },
    content: @Composable (PrezelChipType, T) -> Unit,
) {
    PreviewSection(
        title = previewTitle,
        description = previewDescription,
    ) {
        PrezelChipType.entries.forEach { type ->
            values.forEach { value ->
                PreviewValueRow(
                    name = type.name,
                    valueLabel = valueLabel(value),
                ) {
                    content(type, value)
                }
            }
        }
    }
}

@Composable
internal fun PrezelChipSizePreviewContent(content: @Composable (PrezelChipType, PrezelChipSize) -> Unit) {
    PrezelChipPreviewContent(
        previewTitle = "Chip / Size",
        previewDescription = "Chip의 크기를 조절합니다.",
        values = PrezelChipSize.entries,
        valueLabel = { it.name },
        content = content,
    )
}

@Composable
internal fun PrezelChipInteractionPreviewContent(content: @Composable (PrezelChipType, PrezelChipInteraction) -> Unit) {
    PrezelChipPreviewContent(
        previewTitle = "Chip / Interaction",
        previewDescription = "Chip의 상호작용 상태를 조절합니다.",
        values = PrezelChipInteraction.entries,
        valueLabel = { it.name },
        content = content,
    )
}

@Composable
internal fun PrezelChipFeedbackPreviewContent(content: @Composable (PrezelChipType, PrezelChipFeedback) -> Unit) {
    PrezelChipPreviewContent(
        previewTitle = "Chip / Feedback",
        previewDescription = "Chip의 피드백 상태를 조절합니다.",
        values = PrezelChipFeedback.entries,
        valueLabel = { it.name },
        content = content,
    )
}

@Composable
internal fun PrezelChipCustomPreviewContent() {
    PreviewSection(
        title = "Chip / Custom",
        description = "사용자 정의된 색 지정이 가능합니다.",
    ) {
        PreviewRow {
            PrezelChip(
                text = "느려요",
                iconResId = PrezelIcons.Blank,
                type = PrezelChipType.FILLED,
                size = PrezelChipSize.REGULAR,
                interaction = PrezelChipInteraction.DISABLED,
                feedback = PrezelChipFeedback.BAD,
                customColors = PrezelChipColors(
                    containerColor = PrezelTheme.colors.feedbackWarningSmall,
                    iconColor = PrezelTheme.colors.feedbackWarningRegular,
                    textColor = PrezelTheme.colors.feedbackWarningRegular,
                ),
            )

            PrezelChip(
                text = "빨라요",
                type = PrezelChipType.OUTLINED,
                size = PrezelChipSize.REGULAR,
                interaction = PrezelChipInteraction.DEFAULT,
                feedback = PrezelChipFeedback.DEFAULT,
                customColors = PrezelChipColors(
                    containerColor = PrezelTheme.colors.feedbackBadSmall,
                    borderColor = PrezelTheme.colors.feedbackBadRegular,
                    iconColor = PrezelTheme.colors.feedbackBadRegular,
                    textColor = PrezelTheme.colors.feedbackBadRegular,
                ),
            )

            PrezelChip(
                text = "적당해요",
                type = PrezelChipType.FILLED,
                size = PrezelChipSize.SMALL,
                interaction = PrezelChipInteraction.ACTIVE,
                feedback = PrezelChipFeedback.DEFAULT,
                customColors = PrezelChipColors(
                    containerColor = Color(0xFFDBFFF6),
                    textColor = Color(0xFF00A37A),
                ),
            )
        }
    }
}
