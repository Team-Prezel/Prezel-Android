package com.team.prezel.feature.analysis.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import kotlinx.coroutines.delay

@Composable
internal fun AnalyzingScreen(
    onFinished: () -> Unit,
    onBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(1_200)
        onFinished()
    }

    AnalyzingScreen(onBack = onBack)
}

@Composable
private fun AnalyzingScreen(onBack: () -> Unit) {
    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_analyzing_title),
        progress = 1f,
        buttonText = stringResource(R.string.feature_analysis_impl_analyzing_title),
        buttonEnabled = false,
        onButtonClick = {},
        onBack = onBack,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = PrezelTheme.colors.interactiveRegular)
                Spacer(modifier = Modifier.height(PrezelTheme.spacing.V20))
                Text(
                    text = stringResource(R.string.feature_analysis_impl_analyzing_headline),
                    color = PrezelTheme.colors.textLarge,
                    style = PrezelTheme.typography.title2Bold,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun AnalyzingScreenPreview() {
    PrezelTheme {
        AnalyzingScreen(onBack = {})
    }
}
