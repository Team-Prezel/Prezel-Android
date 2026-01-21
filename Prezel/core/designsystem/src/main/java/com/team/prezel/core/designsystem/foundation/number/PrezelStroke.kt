package com.team.prezel.core.designsystem.foundation.number

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.preview.TokenList
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

object PrezelStroke {
    val V1 = 1.dp
    val V2 = 2.dp
    val V4 = 4.dp
}

@ThemePreview
@Composable
private fun StrokeTokensPreview() {
    PrezelTheme {
        PreviewScaffold {
            StrokeSection()
        }
    }
}

@Composable
private fun StrokeSection() {
    SectionTitle(title = "Stroke")
    TokenList(
        items = persistentListOf(
            "V1" to PrezelStroke.V1,
            "V2" to PrezelStroke.V2,
            "V4" to PrezelStroke.V4,
        ),
        preview = { stroke ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = stroke,
                        color = PrezelTheme.colors.borderLarge,
                        shape = PrezelShapes.V8,
                    ),
            )
        },
    )
}
