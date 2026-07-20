package com.team.prezel.feature.practice.impl.recording.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.practice.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PracticeRecordingTopAppBar(onBack: () -> Unit) {
    PrezelTopAppBar(
        title = stringResource(R.string.feature_practice_impl_practice_recording_title),
    ) {
        LeadingIcon(
            iconResId = PrezelIcons.ArrowLeft,
            contentDescription = stringResource(R.string.feature_practice_impl_practice_recording_back),
            onClick = onBack,
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingTopAppBarPreview() {
    PrezelTheme {
        PracticeRecordingTopAppBar(onBack = {})
    }
}
