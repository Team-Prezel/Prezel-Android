package com.team.prezel.core.designsystem.foundation.number

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.preview.TokenList
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

object PrezelSpacing {
    val V0 = 0.dp
    val V2 = 2.dp
    val V4 = 4.dp
    val V6 = 6.dp
    val V8 = 8.dp
    val V10 = 10.dp
    val V12 = 12.dp
    val V14 = 14.dp
    val V16 = 16.dp
    val V20 = 20.dp
    val V24 = 24.dp
    val V28 = 28.dp
    val V32 = 32.dp
    val V36 = 36.dp
    val V40 = 40.dp
    val V48 = 48.dp
    val V56 = 56.dp
    val V64 = 64.dp
    val V72 = 72.dp
    val V80 = 80.dp
}

@ThemePreview
@Composable
private fun SpacingTokensPreview() {
    PrezelTheme {
        PreviewScaffold {
            SpacingSection()
        }
    }
}

@Composable
private fun SpacingSection() {
    SectionTitle(title = "Spacing")
    TokenList(
        items = persistentListOf(
            "V0" to PrezelSpacing.V0,
            "V2" to PrezelSpacing.V2,
            "V4" to PrezelSpacing.V4,
            "V6" to PrezelSpacing.V6,
            "V8" to PrezelSpacing.V8,
            "V10" to PrezelSpacing.V10,
            "V12" to PrezelSpacing.V12,
            "V14" to PrezelSpacing.V14,
            "V16" to PrezelSpacing.V16,
            "V20" to PrezelSpacing.V20,
            "V24" to PrezelSpacing.V24,
            "V28" to PrezelSpacing.V28,
            "V32" to PrezelSpacing.V32,
            "V36" to PrezelSpacing.V36,
            "V40" to PrezelSpacing.V40,
            "V48" to PrezelSpacing.V48,
            "V56" to PrezelSpacing.V56,
            "V64" to PrezelSpacing.V64,
            "V72" to PrezelSpacing.V72,
            "V80" to PrezelSpacing.V80,
        ),
        preview = { spacing ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(spacing)
                        .background(PrezelTheme.colors.interactiveRegular),
                )
            }
        },
    )
}
