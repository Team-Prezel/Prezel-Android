package com.team.prezel.core.designsystem.foundation.number

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.preview.TokenRow
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

@ThemePreview
@Composable
private fun ShapesTokensPreview() {
    PrezelTheme {
        PreviewScaffold {
            ShapesSection()
        }
    }
}

@Composable
private fun ShapesSection() {
    SectionTitle(title = "Shapes")
    ShapeList(
        items = listOf(
            "V2" to PrezelShapes.V2,
            "V4" to PrezelShapes.V4,
            "V6" to PrezelShapes.V6,
            "V8" to PrezelShapes.V8,
            "V12" to PrezelShapes.V12,
            "V16" to PrezelShapes.V16,
            "V1000" to PrezelShapes.V1000,
        ),
    )
}

@Composable
private fun ShapeList(items: List<Pair<String, androidx.compose.foundation.shape.RoundedCornerShape>>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { (name, shape) ->
            TokenRow(
                name = name,
                valueLabel = "shape",
                preview = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape)
                            .background(PrezelTheme.colors.bgLarge)
                            .border(PrezelStroke.V1, PrezelTheme.colors.borderRegular, shape),
                    )
                },
            )
        }
    }
}
