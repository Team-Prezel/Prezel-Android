package com.team.prezel.feature.practice.impl.result.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PrezelLottie
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.practice.impl.R
import com.team.prezel.core.ui.R as CoreUiR

@Composable
internal fun PracticeRecordingAnalysisLoadingPage(modifier: Modifier = Modifier) {
    StatusView(
        title = stringResource(R.string.feature_practice_impl_practice_recording_analysis_loading_title),
        description = stringResource(R.string.feature_practice_impl_practice_recording_analysis_loading_description),
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
private fun PracticeRecordingAnalysisLoadingPagePreview() {
    PrezelTheme {
        PracticeRecordingAnalysisLoadingPage()
    }
}
