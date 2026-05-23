package com.team.prezel.feature.analysis.impl.result

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PrezelLottie
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.analysis.impl.R
import kotlinx.coroutines.delay
import com.team.prezel.core.ui.R as CoreUiR

private const val ANALYSIS_LOADING_DURATION_MILLIS = 2_000L

@Composable
internal fun AnalysisLoadingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        delay(ANALYSIS_LOADING_DURATION_MILLIS)
        onFinished()
    }

    StatusView(
        title = stringResource(R.string.feature_analysis_impl_analyzing_title),
        description = stringResource(R.string.feature_analysis_impl_analyzing_headline),
        modifier = modifier,
        visual = {
            PrezelLottie(
                resId = CoreUiR.raw.core_ui_asset_loading,
                modifier = Modifier.size(80.dp),
            )
        },
    )
}

@BasicPreview
@Composable
private fun AnalysisLoadingScreenPreview() {
    PrezelTheme {
        AnalysisLoadingScreen(onFinished = {})
    }
}
