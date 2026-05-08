package com.team.prezel.feature.home.impl.practice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@Composable
internal fun PracticeRecordingContent(
    practiceScript: String,
    currentSeconds: Int,
    totalSeconds: Int,
    recordingState: AudioSessionState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onStartPlayback: () -> Unit,
    onStopPlayback: () -> Unit,
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(PrezelTheme.radius.V6))
                .background(PrezelTheme.colors.bgMedium)
                .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V12),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = practiceScript,
                style = PrezelTheme.typography.body2Regular,
                color = when (recordingState) {
                    is AudioSessionState.ReadyToPlay,
                    is AudioSessionState.Playing,
                    -> PrezelTheme.colors.textDisabled

                    AudioSessionState.Idle,
                    is AudioSessionState.Recording,
                    -> PrezelTheme.colors.textLarge
                },
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PracticeRecordingControl(
            currentSeconds = currentSeconds,
            totalSeconds = totalSeconds,
            audioSessionState = recordingState,
            onStartRecording = onStartRecording,
            onStopRecording = onStopRecording,
            onStartPlayback = onStartPlayback,
            onStopPlayback = onStopPlayback,
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingContentReadyToRecordPreview() {
    PrezelTheme {
        PracticeRecordingContent(
            practiceScript = "안녕하세요. 오늘은 제가 준비한 발표 연습을 시작해보겠습니다.",
            currentSeconds = 0,
            totalSeconds = 0,
            recordingState = AudioSessionState.Idle,
            onStartRecording = {},
            onStopRecording = {},
            onStartPlayback = {},
            onStopPlayback = {},
            modifier = Modifier.height(520.dp),
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingContentReadyToPlayPreview() {
    PrezelTheme {
        PracticeRecordingContent(
            practiceScript = "안녕하세요. 오늘은 제가 준비한 발표 연습을 시작해보겠습니다.",
            currentSeconds = 12,
            totalSeconds = 45,
            recordingState = AudioSessionState.ReadyToPlay(
                source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                positionSeconds = 12,
                durationSeconds = 45,
            ),
            onStartRecording = {},
            onStopRecording = {},
            onStartPlayback = {},
            onStopPlayback = {},
            modifier = Modifier.height(520.dp),
        )
    }
}
