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

object PrezelRadius {
    val V2 = 2.dp
    val V4 = 4.dp
    val V6 = 6.dp
    val V8 = 8.dp
    val V12 = 12.dp
    val V16 = 16.dp
    val V1000 = 1000.dp
}

@BasicPreview
@Composable
private fun RadiusTokensPreview() {
    PreviewSection(
        title = "Radius",
        description = "둥근 정도에 사용하는 숫자입니다.",
    ) {
        listOf(
            "V2" to PrezelRadius.V2,
            "V4" to PrezelRadius.V4,
            "V6" to PrezelRadius.V6,
            "V8" to PrezelRadius.V8,
            "V12" to PrezelRadius.V12,
            "V16" to PrezelRadius.V16,
            "V1000" to PrezelRadius.V1000,
        ).forEach { (name, radius) ->
            PreviewValueRow(
                name = name,
                valueLabel = "${radius.value}dp",
            ) {
                val shape = RoundedCornerShape(radius)
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

@Composable
private fun RadiusSection() {
}
