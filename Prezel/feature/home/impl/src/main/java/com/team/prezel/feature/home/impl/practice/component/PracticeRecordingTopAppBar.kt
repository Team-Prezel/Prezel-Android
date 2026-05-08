package com.team.prezel.feature.home.impl.practice.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PracticeRecordingTopAppBar(onBack: () -> Unit) {
    PrezelTopAppBar(
        title = { Text(text = stringResource(R.string.feature_home_impl_practice_recording_title)) },
        leadingIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(PrezelIcons.ArrowLeft),
                    contentDescription = stringResource(R.string.feature_home_impl_practice_recording_back),
                )
            }
        },
    )
}

@BasicPreview
@Composable
private fun PracticeRecordingTopAppBarPreview() {
    PrezelTheme {
        PracticeRecordingTopAppBar(onBack = {})
    }
}
