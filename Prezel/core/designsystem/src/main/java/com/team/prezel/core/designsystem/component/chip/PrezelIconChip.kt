package com.team.prezel.core.designsystem.component.chip

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelIconChip(
    icon: IconSource,
    modifier: Modifier = Modifier,
    style: PrezelChipStyle = PrezelChipStyle(),
) {
    PrezelChip(
        modifier = modifier,
        icon = icon,
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
        icon = IconSource(resId = PrezelIcons.Blank),
        style = style,
    )
}
