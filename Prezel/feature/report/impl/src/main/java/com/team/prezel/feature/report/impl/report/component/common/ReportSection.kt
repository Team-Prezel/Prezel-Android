package com.team.prezel.feature.report.impl.report.component.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun ReportSection(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        title()
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        content()
    }
}

@BasicPreview
@Composable
private fun ReportSectionPreview() {
    PrezelTheme {
        ReportSection(
            title = {
                Text(
                    text = "Section Title",
                    style = PrezelTheme.typography.title2Bold,
                    color = PrezelTheme.colors.textLarge,
                )
            },
            content = {
                Text(
                    text = "This is the content of the report section.",
                    style = PrezelTheme.typography.body2Regular,
                    color = PrezelTheme.colors.textMedium,
                )
            },
        )
    }
}
