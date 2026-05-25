package com.team.prezel.feature.report.impl.component.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun EmptyStateCard(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PrezelTheme.colors.borderSmall,
                shape = PrezelTheme.shapes.V8,
            ).padding(PrezelTheme.spacing.V16),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@BasicPreview
@Composable
private fun EmptyStateCardPreview() {
    PrezelTheme {
        EmptyStateCard(
            text = "대본을 입력하고 맞춤법과 주술호응을 분석해보세요.",
            modifier = Modifier.padding(12.dp),
        )
    }
}
