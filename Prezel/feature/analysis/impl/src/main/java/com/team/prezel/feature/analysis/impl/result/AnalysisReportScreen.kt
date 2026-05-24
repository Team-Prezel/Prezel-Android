package com.team.prezel.feature.analysis.impl.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R

@Composable
internal fun AnalysisReportScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.feature_analysis_impl_report_placeholder),
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.title2Bold,
        )
    }
}

@BasicPreview
@Composable
private fun AnalysisReportScreenPreview() {
    PrezelTheme {
        AnalysisReportScreen()
    }
}
