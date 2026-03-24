package com.team.prezel.core.designsystem.foundation.number

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

object PrezelShapes {
    val V2 = RoundedCornerShape(PrezelRadius.V2)
    val V4 = RoundedCornerShape(PrezelRadius.V4)
    val V6 = RoundedCornerShape(PrezelRadius.V6)
    val V8 = RoundedCornerShape(PrezelRadius.V8)
    val V12 = RoundedCornerShape(PrezelRadius.V12)
    val V16 = RoundedCornerShape(PrezelRadius.V16)
    val V1000 = RoundedCornerShape(PrezelRadius.V1000)
}

@BasicPreview
@Composable
private fun ShapesTokensPreview() {
    PreviewSection(
        title = "Shapes",
    ) {
        listOf(
            "V2" to PrezelShapes.V2,
            "V4" to PrezelShapes.V4,
            "V6" to PrezelShapes.V6,
            "V8" to PrezelShapes.V8,
            "V12" to PrezelShapes.V12,
            "V16" to PrezelShapes.V16,
            "V1000" to PrezelShapes.V1000,
        ).forEach { (name, shape) ->
            PreviewValueRow(name = name) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape)
                        .background(PrezelTheme.colors.bgLarge)
                        .border(
                            width = PrezelStroke.V1,
                            color = PrezelTheme.colors.borderRegular,
                            shape = shape,
                        ),
                )
            }
        }
    }
}
