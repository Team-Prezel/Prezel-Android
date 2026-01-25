package com.team.prezel.core.designsystem.component.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@ThemePreview
@Composable
private fun PrezelChip_CustomColors_Preview() {
    PrezelTheme {
        PreviewScaffold {
            CustomChipHeader()

            Spacer(modifier = Modifier.height(12.dp))

            CustomChipLabelSection()

            Spacer(modifier = Modifier.height(16.dp))

            CustomChipIconOnlySection()
        }
    }
}

@Composable
private fun CustomChipHeader() {
    Text(
        text = "Custom Chip",
        style = PrezelTheme.typography.title2Medium,
    )
}

@Composable
private fun CustomChipLabelSection() {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3,
    ) {
        CustomYellowLabelChip()
        CustomRedLabelChip()
        CustomGreenLabelChip()
    }
}

@Composable
private fun CustomChipIconOnlySection() {
    Text(
        text = "Icon only",
        style = PrezelTheme.typography.body3Medium,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomYellowIconChip()
        CustomRedIconChip()
        CustomGreenIconChip()
    }
}

@Composable
private fun CustomYellowLabelChip() {
    PrezelChip(
        text = "느려요",
        icon = DrawableIcon(resId = PrezelIcons.Blank),
        style = PrezelChipStyle(
            type = PrezelChipType.FILLED,
            size = PrezelChipSize.REGULAR,
            interaction = PrezelChipInteraction.DISABLED,
            feedback = PrezelChipFeedback.BAD,
        ),
        containerColor = PrezelTheme.colors.feedbackWarningSmall,
        contentColor = PrezelTheme.colors.feedbackWarningRegular,
    )
}

@Composable
private fun CustomRedLabelChip() {
    PrezelChip(
        text = "빨라요",
        icon = null,
        style = PrezelChipStyle(
            type = PrezelChipType.OUTLINED,
            size = PrezelChipSize.REGULAR,
            interaction = PrezelChipInteraction.DEFAULT,
            feedback = PrezelChipFeedback.DEFAULT,
        ),
        containerColor = PrezelTheme.colors.feedbackBadSmall,
        contentColor = PrezelTheme.colors.feedbackBadRegular,
    )
}

@Composable
private fun CustomGreenLabelChip() {
    PrezelChip(
        text = "적당해요",
        icon = null,
        style = PrezelChipStyle(
            type = PrezelChipType.FILLED,
            size = PrezelChipSize.SMALL,
            interaction = PrezelChipInteraction.ACTIVE,
            feedback = PrezelChipFeedback.DEFAULT,
        ),
        containerColor = Color(0xFFDBFFF6),
        contentColor = Color(0xFF00A37A),
    )
}

@Composable
private fun CustomYellowIconChip() {
    PrezelChip(
        text = null,
        icon = DrawableIcon(resId = PrezelIcons.Blank),
        style = PrezelChipStyle(
            type = PrezelChipType.FILLED,
            size = PrezelChipSize.REGULAR,
            interaction = PrezelChipInteraction.DISABLED,
            feedback = PrezelChipFeedback.BAD,
        ),
        containerColor = PrezelTheme.colors.feedbackWarningSmall,
        contentColor = PrezelTheme.colors.feedbackWarningRegular,
    )
}

@Composable
private fun CustomRedIconChip() {
    PrezelChip(
        text = null,
        icon = DrawableIcon(resId = PrezelIcons.Blank),
        style = PrezelChipStyle(
            type = PrezelChipType.OUTLINED,
            size = PrezelChipSize.REGULAR,
            interaction = PrezelChipInteraction.DEFAULT,
            feedback = PrezelChipFeedback.DEFAULT,
        ),
        containerColor = PrezelTheme.colors.feedbackBadSmall,
        contentColor = PrezelTheme.colors.feedbackBadRegular,
    )
}

@Composable
private fun CustomGreenIconChip() {
    PrezelChip(
        text = null,
        icon = DrawableIcon(resId = PrezelIcons.Blank),
        style = PrezelChipStyle(
            type = PrezelChipType.FILLED,
            size = PrezelChipSize.SMALL,
            interaction = PrezelChipInteraction.ACTIVE,
            feedback = PrezelChipFeedback.DEFAULT,
        ),
        containerColor = Color(0xFFDBFFF6),
        contentColor = Color(0xFF00A37A),
    )
}
