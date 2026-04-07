package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview

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
) {
    val hasText = text != null
    val hasIcon = iconResId != null
    require(hasText || hasIcon) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }

    Surface(
        modifier = modifier,
        shape = config.shape,
        color = config.containerColor,
        border = config.borderStroke,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides config.textStyle,
            LocalPrezelChipIconColor provides config.iconColor,
            LocalPrezelChipTextColor provides config.textColor,
        ) {
            Row(
                modifier = Modifier.padding(config.contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                iconResId?.let { resId ->
                    PrezelChipIcon(iconResId = resId, config = config)
                }

                if (hasText) {
                    if (hasIcon) {
                        Spacer(modifier = Modifier.width(config.iconTextSpacing))
                    }
                    Text(text = text, color = LocalPrezelChipTextColor.current)
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
    type: PrezelChipType = PrezelChipType.FILLED,
    size: PrezelChipSize = PrezelChipSize.REGULAR,
    interaction: PrezelChipInteraction = PrezelChipInteraction.DEFAULT,
    feedback: PrezelChipFeedback = PrezelChipFeedback.DEFAULT,
    config: PrezelChipDefault? = null,
    customColors: PrezelChipColors = LocalPrezelChipColors.current,
) {
    CompositionLocalProvider(
        LocalPrezelChipColors provides customColors,
    ) {
        val resolvedConfig =
            config ?: PrezelChipDefaults.getDefault(
                iconOnly = text == null && iconResId != null,
                type = type,
                size = size,
                interaction = interaction,
                feedback = feedback,
            )

        PrezelChip(
            modifier = modifier,
            text = text,
            iconResId = iconResId,
            type = type,
            size = size,
            interaction = interaction,
            feedback = feedback,
            config = resolvedConfig,
        )
    }
}

@Composable
private fun PrezelChipIcon(
    @DrawableRes iconResId: Int,
    config: PrezelChipDefault,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = modifier.size(config.iconSize),
        tint = LocalPrezelChipIconColor.current,
    )
}

@BasicPreview
@Composable
private fun PrezelChipSizePreview() {
    PrezelChipSizePreviewContent { type, size ->
        PrezelChip(
            text = "Label",
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
        PrezelChip(
            text = "Label",
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
        PrezelChip(
            text = "Label",
            iconResId = PrezelIcons.Blank,
            type = type,
            feedback = feedback,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelChipCustomPreview() {
    PrezelChipCustomPreviewContent()
}
