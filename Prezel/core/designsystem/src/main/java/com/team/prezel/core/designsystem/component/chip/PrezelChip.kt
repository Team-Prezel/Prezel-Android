package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewRow
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes iconResId: Int? = null,
    style: PrezelChipStyle = PrezelChipStyle(),
) {
    val hasText = text != null
    val hasIcon = iconResId != null
    val iconOnly = hasIcon && !hasText
    require(hasText || hasIcon) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }

    Surface(
        modifier = modifier,
        shape = style.shape(),
        color = style.containerColor(iconOnly = iconOnly),
        border = style.borderStroke(),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides style.textStyle(),
            LocalContentColor provides style.contentColor(),
        ) {
            Row(
                modifier = Modifier.padding(style.contentPadding(iconOnly = iconOnly)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                iconResId?.let { resId ->
                    PrezelChipIcon(iconResId = resId, style = style)
                }

                if (hasText) {
                    if (hasIcon) {
                        Spacer(modifier = Modifier.width(style.iconTextSpacing()))
                    }
                    Text(text = text)
                }
            }
        }
    }
}

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes iconResId: Int? = null,
    style: PrezelChipStyle = PrezelChipStyle(),
    customColors: PrezelChipColors = LocalPrezelChipColors.current,
) {
    CompositionLocalProvider(
        LocalPrezelChipColors provides customColors,
    ) {
        PrezelChip(
            modifier = modifier,
            text = text,
            iconResId = iconResId,
            style = style,
        )
    }
}

@Composable
private fun PrezelChipIcon(
    @DrawableRes iconResId: Int,
    style: PrezelChipStyle,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = modifier.size(style.iconSize()),
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
                    PrezelChip(
                        text = "Label",
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
                    PrezelChip(
                        text = "Label",
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
                    PrezelChip(
                        text = "Label",
                        iconResId = PrezelIcons.Blank,
                        style = PrezelChipStyle(type = type, feedback = feedback),
                    )
                }
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelChipCustomPreview() {
    PreviewSection(
        title = "Chip / Custom",
        description = "사용자 정의된 색 지정이 가능합니다.",
    ) {
        PreviewRow {
            PrezelChip(
                text = "느려요",
                iconResId = PrezelIcons.Blank,
                style = PrezelChipStyle(
                    type = PrezelChipType.FILLED,
                    size = PrezelChipSize.REGULAR,
                    interaction = PrezelChipInteraction.DISABLED,
                    feedback = PrezelChipFeedback.BAD,
                ),
                customColors = PrezelChipColors(
                    containerColor = PrezelTheme.colors.feedbackWarningSmall,
                    contentColor = PrezelTheme.colors.feedbackWarningRegular,
                ),
            )

            PrezelChip(
                text = "빨라요",
                iconResId = null,
                style = PrezelChipStyle(
                    type = PrezelChipType.OUTLINED,
                    size = PrezelChipSize.REGULAR,
                    interaction = PrezelChipInteraction.DEFAULT,
                    feedback = PrezelChipFeedback.DEFAULT,
                ),
                customColors = PrezelChipColors(
                    containerColor = PrezelTheme.colors.feedbackBadSmall,
                    contentColor = PrezelTheme.colors.feedbackBadRegular,
                ),
            )

            PrezelChip(
                text = "적당해요",
                iconResId = null,
                style = PrezelChipStyle(
                    type = PrezelChipType.FILLED,
                    size = PrezelChipSize.SMALL,
                    interaction = PrezelChipInteraction.ACTIVE,
                    feedback = PrezelChipFeedback.DEFAULT,
                ),
                customColors = PrezelChipColors(
                    containerColor = Color(0xFFDBFFF6),
                    contentColor = Color(0xFF00A37A),
                ),
            )
        }
    }
}
