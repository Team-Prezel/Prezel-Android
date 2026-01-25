package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
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

@ThemePreview
@Composable
private fun PrezelDividerPreview() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle(title = "PrezelDivider - Horizontal")
            Text("PrezelDividerType.DEFAULT")
            PrezelHorizontalDivider(type = PrezelDividerType.DEFAULT)
            Text("PrezelDividerType.THICK")
            PrezelHorizontalDivider(type = PrezelDividerType.THICK)

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle(title = "PrezelDivider - Vertical")
            Text("PrezelDividerType.DEFAULT")
            PrezelVerticalDivider(type = PrezelDividerType.DEFAULT, modifier = Modifier.height(100.dp))
            Text("PrezelDividerType.THICK")
            PrezelVerticalDivider(type = PrezelDividerType.THICK, modifier = Modifier.height(100.dp))
        }
    }
}
