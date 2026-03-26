package com.team.prezel.core.designsystem.component.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.util.drawDashBorder

/**
 * 시각 크기보다 넓은 터치 여유 영역을 줄 수 있는 클릭 컨테이너입니다.
 *
 * `extraTouchPadding`으로 터치 영역을 확보하고 필요할 때 ripple 표시를 끌 수 있습니다.
 */
@Composable
fun PrezelTouchArea(
    modifier: Modifier = Modifier,
    extraTouchPadding: PaddingValues = PaddingValues(0.dp),
    shape: Shape = RectangleShape,
    enabled: Boolean = true,
    rippleColor: Color = Color.Unspecified,
    isUseRipple: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape = shape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                indication = if (isUseRipple) ripple(color = rippleColor) else null,
                interactionSource = interactionSource,
            ).padding(extraTouchPadding),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@BasicPreview
@Composable
private fun PrezelTouchAreaPreview() {
    PreviewSection(
        title = "Touch Area",
        description = "시각 크기와 별도로 터치 가능한 영역을 확인합니다.",
    ) {
        PreviewValueRow(name = "Extra Touch Padding (0.dp)") {
            PrezelTouchArea(onClick = {}) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Cyan),
                )
            }
        }

        PreviewValueRow(name = "Extra Touch Padding (12.dp)") {
            PrezelTouchArea(
                onClick = {},
                extraTouchPadding = PaddingValues(12.dp),
                modifier = Modifier.drawDashBorder(),
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Cyan),
                )
            }
        }
    }
}
