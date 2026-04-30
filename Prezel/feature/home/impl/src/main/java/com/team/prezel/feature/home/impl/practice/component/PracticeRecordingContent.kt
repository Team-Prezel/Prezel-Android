package com.team.prezel.feature.home.impl.practice.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@Composable
internal fun PracticeRecordingContent(
    practiceScript: String,
    currentSeconds: Int,
    totalSeconds: Int,
    controlState: PracticeRecordingControlState,
    onClickControl: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V20, vertical = PrezelTheme.spacing.V16),
    ) {
        Text(
            text = stringResource(R.string.feature_home_impl_practice_recording_instruction),
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PracticeScriptCard(
            text = practiceScript,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PracticeRecordingControl(
            currentSeconds = currentSeconds,
            totalSeconds = totalSeconds,
            state = controlState,
            onClickControl = onClickControl,
        )
    }
}
