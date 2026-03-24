package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSurface

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
private fun PrezelIconChipPreview() {
    PreviewSurface {
        PreviewColumn(scrollable = true) {
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
