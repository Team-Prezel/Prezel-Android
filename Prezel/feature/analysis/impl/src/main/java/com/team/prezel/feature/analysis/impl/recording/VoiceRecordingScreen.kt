package com.team.prezel.feature.analysis.impl.recording

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.common.event.EdgeToEdgeStatusBarStyle
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay

@Composable
internal fun VoiceRecordingScreen(
    uiState: AnalysisFlowUiState,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
    onBack: () -> Unit,
) {
    VoiceRecordingScreen(
        script = uiState.form.script,
        recordingState = uiState.recordingState,
        analyzeEnabled = uiState.canMoveNext,
        onClickRecordingControl = onClickRecordingControl,
        onStopRecording = onStopRecording,
        onResetRecording = onResetRecording,
        onAnalyze = onAnalyze,
        onBack = onBack,
    )
}

@Composable
private fun VoiceRecordingScreen(
    script: String,
    recordingState: AudioSessionState,
    analyzeEnabled: Boolean,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
    onBack: () -> Unit,
) {
    VoiceRecordingStatusBarStyle(
        style = if (recordingState.isCompleted) {
            EdgeToEdgeStatusBarStyle.BG_REGULAR
        } else {
            EdgeToEdgeStatusBarStyle.BG_MEDIUM
        },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        if (recordingState.isCompleted) {
            VoiceRecordingCompletedTopBar(onBack = onBack)
        } else {
            VoiceRecordingHeader(
                recordingState = recordingState,
                onBack = onBack,
            )
        }

        VoiceRecordingContent(
            script = script,
            recordingState = recordingState,
            onClickRecordingControl = onClickRecordingControl,
            modifier = Modifier.weight(1f),
        )

        VoiceRecordingButtonArea(
            recordingState = recordingState,
            analyzeEnabled = analyzeEnabled,
            onClickRecordingControl = onClickRecordingControl,
            onStopRecording = onStopRecording,
            onResetRecording = onResetRecording,
            onAnalyze = onAnalyze,
        )
    }
}

@Composable
private fun VoiceRecordingStatusBarStyle(style: EdgeToEdgeStatusBarStyle) {
    if (LocalInspectionMode.current) return

    val globalEventBus = rememberGlobalEventBus()

    LaunchedEffect(globalEventBus, style) {
        globalEventBus.emit(GlobalEvent.ChangeEdgeToEdgeStatusBarStyle(style))
    }

    DisposableEffect(globalEventBus) {
        onDispose {
            globalEventBus.tryEmit(GlobalEvent.ResetEdgeToEdgeStatusBarStyle)
        }
    }
}

@Composable
private fun rememberGlobalEventBus(): GlobalEventBus {
    val applicationContext = LocalContext.current.applicationContext

    return remember(applicationContext) {
        EntryPointAccessors
            .fromApplication(
                applicationContext,
                VoiceRecordingGlobalEventBusEntryPoint::class.java,
            ).globalEventBus()
    }
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
                .statusBarsPadding()
                .padding(top = PrezelTheme.spacing.V4, end = PrezelTheme.spacing.V8),
        )
    }
}

@Composable
private fun VoiceRecordingHeader(
    recordingState: AudioSessionState,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(252.dp)
            .background(PrezelTheme.colors.bgMedium),
    ) {
        VoiceRecordingCloseButton(
            onBack = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = PrezelTheme.spacing.V4, end = PrezelTheme.spacing.V8),
        )

        Text(
            text = stringResource(recordingState.titleResId),
            color = PrezelTheme.colors.interactiveRegular,
            style = PrezelTheme.typography.title1Bold,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
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
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = PrezelTheme.colors.iconRegular,
        )
    }
}

@Composable
private fun VoiceRecordingContent(
    script: String,
    recordingState: AudioSessionState,
    onClickRecordingControl: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgRegular)
            .padding(vertical = PrezelTheme.spacing.V16),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!recordingState.isCompleted) {
            VoiceRecordingScriptHeader()
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        }

        VoiceRecordingScriptBody(
            script = script,
            modifier = Modifier.weight(1f),
        )

        if (recordingState !is AudioSessionState.Idle) {
            VoiceRecordingStatusArea(
                recordingState = recordingState,
                onClickRecordingControl = onClickRecordingControl,
            )
        }
    }
}

@Composable
private fun VoiceRecordingScriptHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(horizontal = PrezelTheme.spacing.V20),
    ) {
        Text(
            text = stringResource(R.string.feature_analysis_impl_voice_recording_script_label),
            color = PrezelTheme.colors.textMedium,
            style = PrezelTheme.typography.body3Medium,
            modifier = Modifier.align(Alignment.CenterStart),
        )

        ScriptZoomButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(48.dp),
        )
    }
}

@Composable
private fun VoiceRecordingScriptBody(
    script: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V20),
    ) {
        val backgroundColor = PrezelTheme.colors.bgRegular

        Text(
            text = script.ifBlank { stringResource(R.string.feature_analysis_impl_voice_recording_no_script) },
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.body2Regular,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        )

        VoiceRecordingScriptGradient(
            modifier = Modifier.align(Alignment.TopCenter),
            brush = Brush.verticalGradient(
                colors = listOf(backgroundColor, Color.Transparent),
            ),
        )
        VoiceRecordingScriptGradient(
            modifier = Modifier.align(Alignment.BottomCenter),
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, backgroundColor),
            ),
        )
    }
}

@Composable
private fun VoiceRecordingScriptGradient(
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(brush),
    )
}

@Composable
private fun VoiceRecordingStatusArea(
    recordingState: AudioSessionState,
    onClickRecordingControl: () -> Unit,
) {
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
    RecordingWaveform(modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(recordingState.recordingStatusSpacing))

    if (recordingState.isCompleted) {
        RecordingPlayerControl(
            recordingState = recordingState,
            onClickRecordingControl = onClickRecordingControl,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PrezelTheme.spacing.V20),
        )
    } else {
        RecordingTimer(
            currentSeconds = recordingState.currentSeconds,
            totalSeconds = recordingState.totalSeconds,
            recordingState = recordingState,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ScriptZoomButton(modifier: Modifier = Modifier) {
    IconButton(
        modifier = modifier,
        onClick = {},
    ) {
        Icon(
            painter = painterResource(PrezelIcons.ZoomIn),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = PrezelTheme.colors.iconRegular,
        )
    }
}

@Composable
private fun RecordingWaveform(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .height(60.dp)
            .background(color = PrezelTheme.colors.bgLarge),
    )
}

@Composable
private fun RecordingTimer(
    currentSeconds: Int,
    totalSeconds: Int,
    recordingState: AudioSessionState,
    modifier: Modifier = Modifier,
) {
    val timerText = when (recordingState) {
        is AudioSessionState.ReadyToPlay -> buildAnnotatedString {
            withStyle(SpanStyle(color = PrezelTheme.colors.textLarge)) {
                append(totalSeconds.toTimerText())
            }
        }

        is AudioSessionState.Playing -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = PrezelTheme.colors.interactiveRegular)) {
                    append(currentSeconds.toTimerText())
                }
                withStyle(SpanStyle(color = PrezelTheme.colors.textSmall)) {
                    append("/")
                    append(totalSeconds.toTimerText())
                }
            }
        }

        else -> buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = if (recordingState is AudioSessionState.PausedRecording) {
                        PrezelTheme.colors.textDisabled
                    } else {
                        PrezelTheme.colors.textMedium
                    },
                ),
            ) {
                append(currentSeconds.toTimerText())
            }
        }
    }

    Text(
        text = timerText,
        modifier = modifier,
        color = PrezelTheme.colors.textMedium,
        textAlign = if (recordingState.isCompleted) TextAlign.Start else TextAlign.Center,
        style = PrezelTheme.typography.title1Medium,
    )
}

@Composable
private fun RecordingPlayerControl(
    recordingState: AudioSessionState,
    onClickRecordingControl: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RecordingTimer(
            currentSeconds = recordingState.currentSeconds,
            totalSeconds = recordingState.totalSeconds,
            recordingState = recordingState,
            modifier = Modifier.weight(1f),
        )

        RecordingRoundIconButton(
            iconResId = recordingState.actionIconResId,
            iconColor = recordingState.actionIconColor,
            onClick = onClickRecordingControl,
        )
    }
}

@Composable
private fun VoiceRecordingButtonArea(
    recordingState: AudioSessionState,
    analyzeEnabled: Boolean,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
) {
    when (recordingState) {
        AudioSessionState.Idle -> IdleRecordingButtonArea(
            recordingState = recordingState,
            onClickRecordingControl = onClickRecordingControl,
        )

        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> ActiveRecordingButtonArea(
            recordingState = recordingState,
            onClickRecordingControl = onClickRecordingControl,
            onStopRecording = onStopRecording,
        )

        is AudioSessionState.ReadyToPlay,
        is AudioSessionState.Playing,
        -> CompletedRecordingButtonArea(
            analyzeEnabled = analyzeEnabled,
            onResetRecording = onResetRecording,
            onAnalyze = onAnalyze,
        )
    }
}

@Composable
private fun IdleRecordingButtonArea(
    recordingState: AudioSessionState,
    onClickRecordingControl: () -> Unit,
) {
    PrezelButtonArea(
        showBackground = true,
        mainButton = { buttonModifier ->
            RecordingIconButton(
                iconResId = recordingState.actionIconResId,
                iconColor = recordingState.actionIconColor,
                modifier = buttonModifier,
                onClick = onClickRecordingControl,
            )
        },
    )
}

@Composable
private fun ActiveRecordingButtonArea(
    recordingState: AudioSessionState,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
) {
    PrezelButtonArea(
        showBackground = true,
        isVertical = false,
        isStrongStrength = false,
        mainButton = { buttonModifier ->
            RecordingIconButton(
                iconResId = PrezelIcons.Stop,
                iconColor = PrezelTheme.colors.iconRegular,
                modifier = buttonModifier,
                onClick = onStopRecording,
            )
        },
        subButton = { buttonModifier ->
            RecordingIconButton(
                iconResId = recordingState.actionIconResId,
                iconColor = recordingState.actionIconColor,
                modifier = buttonModifier,
                onClick = onClickRecordingControl,
            )
        },
    )
}

@Composable
private fun CompletedRecordingButtonArea(
    analyzeEnabled: Boolean,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
) {
    PrezelButtonArea(
        showBackground = true,
        isVertical = false,
        mainButton = { buttonModifier ->
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_analyze),
                modifier = buttonModifier,
                enabled = analyzeEnabled,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.PRIMARY,
                onClick = onAnalyze,
            )
        },
        subButton = { buttonModifier ->
            RecordingResetButton(
                iconResId = PrezelIcons.Reset,
                iconColor = PrezelTheme.colors.iconRegular,
                modifier = buttonModifier.width(52.dp),
                onClick = onResetRecording,
            )
        },
    )
}

@Composable
private fun RecordingRoundIconButton(
    @DrawableRes iconResId: Int,
    iconColor: Color,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        modifier = Modifier.size(48.dp),
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            isRounded = true,
            type = ButtonType.FILLED,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.SECONDARY,
            contentColor = iconColor,
            backgroundColor = PrezelTheme.colors.bgLarge,
            iconSize = 20.dp,
        ),
        onClick = onClick,
    )
}

@Composable
private fun RecordingResetButton(
    @DrawableRes iconResId: Int,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        modifier = modifier.height(48.dp),
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            isRounded = false,
            type = ButtonType.GHOST,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.SECONDARY,
            contentColor = iconColor,
            backgroundColor = Color.Transparent,
            iconSize = 20.dp,
        ),
        onClick = onClick,
    )
}

@Composable
private fun RecordingIconButton(
    @DrawableRes iconResId: Int,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(PrezelTheme.radius.V8)),
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            type = ButtonType.FILLED,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.SECONDARY,
            isRounded = false,
            contentColor = iconColor,
            backgroundColor = PrezelTheme.colors.bgLarge,
            iconSize = 20.dp,
        ),
        onClick = onClick,
    )
}

private val AudioSessionState.currentSeconds: Int
    get() = when (this) {
        AudioSessionState.Idle -> 0
        is AudioSessionState.Recording -> elapsedSeconds
        is AudioSessionState.PausedRecording -> elapsedSeconds
        is AudioSessionState.ReadyToPlay -> positionSeconds
        is AudioSessionState.Playing -> positionSeconds
    }

private val AudioSessionState.totalSeconds: Int
    get() = when (this) {
        AudioSessionState.Idle,
        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> 0

        is AudioSessionState.ReadyToPlay -> durationSeconds
        is AudioSessionState.Playing -> durationSeconds
    }

private val AudioSessionState.isCompleted: Boolean
    get() = this is AudioSessionState.ReadyToPlay || this is AudioSessionState.Playing

private val AudioSessionState.recordingStatusSpacing
    @Composable get() = if (isCompleted) PrezelTheme.spacing.V12 else PrezelTheme.spacing.V8

private val AudioSessionState.titleResId: Int
    get() = when (this) {
        AudioSessionState.Idle -> R.string.feature_analysis_impl_voice_recording_ready_title
        is AudioSessionState.Recording -> R.string.feature_analysis_impl_voice_recording_recording_title
        is AudioSessionState.PausedRecording -> R.string.feature_analysis_impl_voice_recording_paused_title
        is AudioSessionState.ReadyToPlay -> R.string.feature_analysis_impl_voice_recording_playing_title
        is AudioSessionState.Playing -> R.string.feature_analysis_impl_voice_recording_playing_title
    }

private val AudioSessionState.actionIconResId: Int
    get() = when (this) {
        AudioSessionState.Idle -> PrezelIcons.Recording
        is AudioSessionState.Recording -> PrezelIcons.Pause
        is AudioSessionState.PausedRecording -> PrezelIcons.Recording
        is AudioSessionState.ReadyToPlay -> PrezelIcons.Play
        is AudioSessionState.Playing -> PrezelIcons.Pause
    }

private val AudioSessionState.actionIconColor: Color
    @Composable get() = when (this) {
        AudioSessionState.Idle -> PrezelTheme.colors.feedbackBadRegular
        is AudioSessionState.PausedRecording -> PrezelTheme.colors.feedbackBadRegular
        is AudioSessionState.Recording,
        is AudioSessionState.ReadyToPlay,
        is AudioSessionState.Playing,
        -> PrezelTheme.colors.iconRegular
    }

private fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

@EntryPoint
@InstallIn(SingletonComponent::class)
private interface VoiceRecordingGlobalEventBusEntryPoint {
    fun globalEventBus(): GlobalEventBus
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
private fun VoiceRecordingScreenPreviewContent(recordingState: AudioSessionState) {
    VoiceRecordingScreen(
        uiState = AnalysisFlowUiState(
            step = AnalysisFlowStep.VOICE_RECORDING,
            form = AnalysisForm(script = "한 번쯤 발표하면서 긴장하신 경험 있으시죠. 오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다."),
            recordingState = recordingState,
        ),
        onClickRecordingControl = {},
        onStopRecording = {},
        onResetRecording = {},
        onAnalyze = {},
        onBack = {},
    )
}

@BasicPreview
@Composable
private fun VoiceRecordingScreenInteractiveFlowPreview() {
    var recordingState by remember { mutableStateOf<AudioSessionState>(AudioSessionState.Idle) }

    LaunchedEffect(recordingState) {
        while (recordingState is AudioSessionState.Recording) {
            delay(1_000)
            recordingState = when (val state = recordingState) {
                is AudioSessionState.Recording -> state.copy(elapsedSeconds = state.elapsedSeconds + 1)
                else -> state
            }
        }
    }

    PrezelTheme {
        VoiceRecordingScreen(
            script = "한 번쯤 발표하면서 긴장하신 경험 있으시죠. 오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
            recordingState = recordingState,
            analyzeEnabled = recordingState.isCompleted,
            onClickRecordingControl = {
                recordingState = when (val state = recordingState) {
                    AudioSessionState.Idle -> AudioSessionState.Recording(elapsedSeconds = 0)
                    is AudioSessionState.Recording -> AudioSessionState.PausedRecording(elapsedSeconds = state.elapsedSeconds)
                    is AudioSessionState.PausedRecording -> AudioSessionState.Recording(elapsedSeconds = state.elapsedSeconds)
                    is AudioSessionState.ReadyToPlay -> AudioSessionState.Playing(
                        source = state.source,
                        durationSeconds = state.durationSeconds,
                        positionSeconds = 0,
                    )

                    is AudioSessionState.Playing -> AudioSessionState.ReadyToPlay(
                        source = state.source,
                        durationSeconds = state.durationSeconds,
                        positionSeconds = state.positionSeconds,
                    )
                }
            },
            onStopRecording = {
                recordingState = when (val state = recordingState) {
                    is AudioSessionState.Recording -> AudioSessionState.ReadyToPlay(
                        source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                        durationSeconds = state.elapsedSeconds.coerceAtLeast(1),
                    )

                    is AudioSessionState.PausedRecording -> AudioSessionState.ReadyToPlay(
                        source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                        durationSeconds = state.elapsedSeconds.coerceAtLeast(1),
                    )

                    else -> state
                }
            },
            onResetRecording = {
                recordingState = AudioSessionState.Idle
            },
            onAnalyze = {},
            onBack = {},
        )
    }
}
