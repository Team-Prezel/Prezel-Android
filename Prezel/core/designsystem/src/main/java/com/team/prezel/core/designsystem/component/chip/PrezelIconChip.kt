package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

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

@ThemePreview
@Composable
private fun PrezelIconChipPreview() {
    PrezelTheme {
        PreviewScaffold {
            PrezelChipPreviewByType(
                type = PrezelChipType.FILLED,
            ) { style -> PrezelIconChipPreviewItem(style) }

            HorizontalDivider()

            PrezelChipPreviewByType(
                type = PrezelChipType.OUTLINED,
            ) { style -> PrezelIconChipPreviewItem(style) }
        }
    }
}

@Composable
private fun PrezelIconChipPreviewItem(style: PrezelChipStyle) {
    PrezelIconChip(
        iconResId = PrezelIcons.Blank,
        style = style,
    )
}
