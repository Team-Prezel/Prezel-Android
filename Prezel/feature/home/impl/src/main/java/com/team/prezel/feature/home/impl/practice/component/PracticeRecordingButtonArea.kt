package com.team.prezel.feature.home.impl.practice.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.feature.home.impl.R

@Composable
internal fun PracticeRecordingButtonArea(
    enabled: Boolean,
    onClickAnalyze: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val analyzeLabel = stringResource(R.string.feature_home_impl_practice_recording_analyze)

    PrezelButtonArea(modifier = modifier) {
        MainButton(
            label = analyzeLabel,
            enabled = enabled,
            onClick = onClickAnalyze,
        )
    }
}
