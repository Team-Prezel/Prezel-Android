package com.team.prezel.feature.analysis.impl.recording

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.designsystem.component.voice.PrezelVoiceChrome
import com.team.prezel.core.designsystem.component.voice.VoiceChromeGradient
import com.team.prezel.core.designsystem.component.voice.VoiceChromeStatus
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay

@Composable
internal fun VoiceRecordingScreen(
    uiState: AnalysisFlowUiState,
    isScriptExpanded: Boolean,
    voiceChromeUi: VoiceRecordingChromeUi? = null,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
    onScriptExpandedChange: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    VoiceRecordingScreen(
        script = uiState.form.script,
        recordingState = uiState.recordingState,
        recordingVolumes = uiState.recordingVolumes,
        voiceChromeUi = voiceChromeUi,
        analyzeEnabled = uiState.canMoveNext,
        isScriptExpanded = isScriptExpanded,
        onClickRecordingControl = onClickRecordingControl,
        onStopRecording = onStopRecording,
        onResetRecording = onResetRecording,
        onAnalyze = onAnalyze,
        onScriptExpandedChange = onScriptExpandedChange,
        onBack = onBack,
    )
}

@Composable
private fun VoiceRecordingScreen(
    script: String,
    recordingState: AudioSessionState,
    recordingVolumes: ImmutableList<Float>,
    voiceChromeUi: VoiceRecordingChromeUi? = null,
    analyzeEnabled: Boolean,
    isScriptExpanded: Boolean,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
    onScriptExpandedChange: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    val useEmptyScriptLayout = script.isBlank() && !isScriptExpanded && !recordingState.isCompleted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        if (!isScriptExpanded) {
            VoiceRecordingTopBar(
                recordingState = recordingState,
                voiceChromeUi = voiceChromeUi,
                expandChrome = useEmptyScriptLayout,
                onBack = onBack,
            )
        }

        val contentModifier = if (useEmptyScriptLayout) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.weight(1f)
        }
        VoiceRecordingContent(
            script = script,
            recordingState = recordingState,
            recordingVolumes = recordingVolumes,
            voiceChromeUi = voiceChromeUi,
            isScriptExpanded = isScriptExpanded,
            onToggleScriptExpanded = {
                onScriptExpandedChange(!isScriptExpanded)
            },
            onClickRecordingControl = onClickRecordingControl,
            useMinimumScriptHeight = useEmptyScriptLayout,
            modifier = contentModifier,
        )

        VoiceRecordingButtonArea(
            recordingState = recordingState,
            analyzeEnabled = analyzeEnabled,
            modifier = Modifier.background(
                if (isScriptExpanded || recordingState.isCompleted) {
                    PrezelTheme.colors.bgRegular
                } else {
                    PrezelTheme.colors.bgMedium
                },
            ),
            onClickRecordingControl = onClickRecordingControl,
            onStopRecording = onStopRecording,
            onResetRecording = onResetRecording,
            onAnalyze = onAnalyze,
        )
    }
}

@Composable
private fun ColumnScope.VoiceRecordingTopBar(
    recordingState: AudioSessionState,
    voiceChromeUi: VoiceRecordingChromeUi?,
    expandChrome: Boolean,
    onBack: () -> Unit,
) {
    if (recordingState.isCompleted) {
        VoiceRecordingCompletedTopBar(onBack = onBack)
        return
    }

    VoiceRecordingChromeTopBar(
        titleResId = voiceChromeUi?.titleResId ?: recordingState.titleResId,
        status = voiceChromeUi?.status ?: recordingState.toVoiceChromeStatus(),
        gradient = voiceChromeUi?.gradient ?: VoiceChromeGradient.MIN,
        expandChrome = expandChrome,
        onBack = onBack,
    )
}

@Composable
private fun VoiceRecordingCompletedTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(PrezelTheme.colors.bgRegular),
    ) {
        VoiceRecordingCloseButton(
            onBack = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = PrezelTheme.spacing.V4, end = PrezelTheme.spacing.V8),
        )
    }
}

@Composable
private fun ColumnScope.VoiceRecordingChromeTopBar(
    @StringRes titleResId: Int,
    status: VoiceChromeStatus,
    gradient: VoiceChromeGradient,
    expandChrome: Boolean,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (expandChrome) Modifier.weight(1f) else Modifier)
            .background(PrezelTheme.colors.bgMedium),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            VoiceRecordingCloseButton(
                onBack = onBack,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = PrezelTheme.spacing.V4, end = PrezelTheme.spacing.V8),
            )
        }

        PrezelVoiceChrome(
            titleText = stringResource(titleResId),
            status = status,
            gradient = gradient,
            modifier = if (expandChrome) {
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            } else {
                Modifier.fillMaxWidth()
            },
        )
    }
}

@Composable
private fun VoiceRecordingCloseButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier.size(48.dp),
        onClick = onBack,
    ) {
        Icon(
            painter = painterResource(PrezelIcons.Cancel),
            contentDescription = stringResource(R.string.feature_analysis_impl_close),
            modifier = Modifier.size(24.dp),
            tint = PrezelTheme.colors.iconRegular,
        )
    }
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenIdlePreview() {
    PrezelTheme {
        VoiceRecordingScreenPreviewContent(AudioSessionState.Idle)
    }
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenRecordingPreview() {
    PrezelTheme {
        VoiceRecordingScreenPreviewContent(AudioSessionState.Recording(elapsedSeconds = 12))
    }
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenEmptyScriptPreview() {
    PrezelTheme {
        VoiceRecordingScreenPreviewContent(
            recordingState = AudioSessionState.Idle,
            script = "",
        )
    }
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenPausedPreview() {
    PrezelTheme {
        VoiceRecordingScreenPreviewContent(AudioSessionState.PausedRecording(elapsedSeconds = 754))
    }
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenCompletedPreview() {
    PrezelTheme {
        VoiceRecordingScreenPreviewContent(
            AudioSessionState.ReadyToPlay(
                source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                durationSeconds = 1_232,
            ),
        )
    }
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenPlayingPreview() {
    PrezelTheme {
        VoiceRecordingScreenPreviewContent(
            AudioSessionState.Playing(
                source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                durationSeconds = 1_232,
                positionSeconds = 12,
            ),
        )
    }
}

@Composable
private fun VoiceRecordingScreenPreviewContent(
    recordingState: AudioSessionState,
    script: String = "한 번쯤 발표하면서 긴장하신 경험 있으시죠. 오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
) {
    VoiceRecordingScreen(
        uiState = AnalysisFlowUiState(
            step = AnalysisFlowStep.VOICE_RECORDING,
            form = AnalysisForm(script = script),
            recordingState = recordingState,
        ),
        isScriptExpanded = false,
        onClickRecordingControl = {},
        onStopRecording = {},
        onResetRecording = {},
        onAnalyze = {},
        onScriptExpandedChange = {},
        onBack = {},
    )
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenInteractiveFlowPreview() {
    var recordingState by remember { mutableStateOf<AudioSessionState>(AudioSessionState.Idle) }

    LaunchedEffect(recordingState) {
        while (recordingState is AudioSessionState.Recording || recordingState is AudioSessionState.Playing) {
            delay(1_000)
            recordingState = recordingState.tick()
        }
    }

    PrezelTheme {
        VoiceRecordingScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.VOICE_RECORDING,
                form = AnalysisForm(script = "한 번쯤 발표하면서 긴장하신 경험 있으시죠. 오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다."),
                recordingState = recordingState,
            ),
            isScriptExpanded = false,
            onClickRecordingControl = { recordingState = recordingState.nextControlState() },
            onStopRecording = { recordingState = recordingState.stopPreviewRecording() },
            onResetRecording = { recordingState = AudioSessionState.Idle },
            onAnalyze = {},
            onScriptExpandedChange = {},
            onBack = {},
        )
    }
}

private fun AudioSessionState.tick(): AudioSessionState =
    when (this) {
        is AudioSessionState.Recording -> copy(elapsedSeconds = elapsedSeconds + 1)
        is AudioSessionState.Playing -> copy(positionSeconds = (positionSeconds + 1).coerceAtMost(durationSeconds))
        else -> this
    }

private fun AudioSessionState.nextControlState(): AudioSessionState =
    when (this) {
        AudioSessionState.Idle -> AudioSessionState.Recording(elapsedSeconds = 0)
        is AudioSessionState.Recording -> AudioSessionState.PausedRecording(elapsedSeconds = elapsedSeconds)
        is AudioSessionState.PausedRecording -> AudioSessionState.Recording(elapsedSeconds = elapsedSeconds)
        is AudioSessionState.ReadyToPlay -> AudioSessionState.Playing(
            source = source,
            durationSeconds = durationSeconds,
            positionSeconds = 0,
        )

        is AudioSessionState.Playing -> AudioSessionState.ReadyToPlay(
            source = source,
            durationSeconds = durationSeconds,
            positionSeconds = positionSeconds,
        )
    }

private fun AudioSessionState.stopPreviewRecording(): AudioSessionState =
    when (this) {
        is AudioSessionState.Recording -> AudioSessionState.ReadyToPlay(
            source = AudioSource.RecordedFile(filePath = "preview.m4a"),
            durationSeconds = elapsedSeconds.coerceAtLeast(1),
        )

        is AudioSessionState.PausedRecording -> AudioSessionState.ReadyToPlay(
            source = AudioSource.RecordedFile(filePath = "preview.m4a"),
            durationSeconds = elapsedSeconds.coerceAtLeast(1),
        )

        else -> this
    }
