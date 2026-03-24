package com.team.prezel.core.designsystem.foundation.number

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

object PrezelStroke {
    val V1 = 1.dp
    val V2 = 2.dp
    val V4 = 4.dp
}

@BasicPreview
@Composable
private fun StrokeTokensPreview() {
    PreviewSection(
        title = "Stroke",
        description = "테두리 굵기에 사용하는 숫자입니다.",
    ) {
        listOf(
            "V1" to PrezelStroke.V1,
            "V2" to PrezelStroke.V2,
            "V4" to PrezelStroke.V4,
        ).forEach { (name, stroke) ->
            PreviewValueRow(
                name = name,
                valueLabel = "${stroke.value}dp",
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = stroke,
                            color = PrezelTheme.colors.borderLarge,
                        ),
                )
            }
        }
    }
}
