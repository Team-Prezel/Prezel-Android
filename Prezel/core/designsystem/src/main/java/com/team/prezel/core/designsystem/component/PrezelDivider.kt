package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class PrezelDividerType(
    val size: Dp,
) {
    DEFAULT(size = 12.dp),
    THICK(size = 1.dp),
    ;

    @Composable
    fun dividerColor(): Color =
        when (this) {
            DEFAULT -> PrezelTheme.colors.borderMedium
            THICK -> PrezelTheme.colors.borderSmall
        }
}

@Composable
fun PrezelHorizontalDivider(
    modifier: Modifier = Modifier,
    type: PrezelDividerType = PrezelDividerType.DEFAULT,
    color: Color = Color.Unspecified,
) = PrezelDivider(modifier = modifier, type = type, isHorizontal = true, color = color)

@Composable
fun PrezelVerticalDivider(
    modifier: Modifier = Modifier,
    type: PrezelDividerType = PrezelDividerType.DEFAULT,
    color: Color = Color.Unspecified,
) = PrezelDivider(modifier = modifier, type = type, isHorizontal = false, color = color)

@Composable
private fun PrezelDivider(
    modifier: Modifier = Modifier,
    type: PrezelDividerType = PrezelDividerType.DEFAULT,
    isHorizontal: Boolean = true,
    color: Color = Color.Unspecified,
) {
    val dividerColor = if (color == Color.Unspecified) type.dividerColor() else color

    when (isHorizontal) {
        true -> HorizontalDivider(modifier = modifier, thickness = type.size, color = dividerColor)
        false -> VerticalDivider(modifier = modifier, thickness = type.size, color = dividerColor)
    }
}

@BasicPreview
@Composable
private fun PrezelHorizontalDividerPreview() {
    PreviewSection(
        title = "PrezelDivider - Horizontal",
        description = "Divider는 두 요소 사이를 구분합니다.",
    ) {
        PreviewValueRow(name = "DEFAULT") {
            Box(Modifier.width(100.dp)) {
                PrezelHorizontalDivider(type = PrezelDividerType.DEFAULT)
            }
        }
        PreviewValueRow(name = "THICK") {
            Box(Modifier.width(100.dp)) {
                PrezelHorizontalDivider(type = PrezelDividerType.THICK)
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelVerticalDividerPreview() {
    PreviewSection(
        title = "PrezelDivider - Vertical",
        description = "Divider는 두 요소 사이를 구분합니다.",
    ) {
        PreviewValueRow(name = "DEFAULT", modifier = Modifier.height(100.dp)) {
            PrezelVerticalDivider(type = PrezelDividerType.DEFAULT)
        }
        PreviewValueRow(name = "THICK", modifier = Modifier.height(100.dp)) {
            PrezelVerticalDivider(type = PrezelDividerType.THICK)
        }
    }
}
