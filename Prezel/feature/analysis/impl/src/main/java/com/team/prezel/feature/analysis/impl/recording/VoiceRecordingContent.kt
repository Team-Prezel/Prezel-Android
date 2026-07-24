package com.team.prezel.feature.analysis.impl.recording

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.component.voice.PrezelVoiceChromeWave
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.analysis.impl.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun VoiceRecordingContent(
    script: String,
    recordingState: AudioSessionState,
    recordingVolumes: ImmutableList<Float>,
    isScriptExpanded: Boolean,
    onToggleScriptExpanded: () -> Unit,
    onClickRecordingControl: () -> Unit,
    modifier: Modifier = Modifier,
    voiceChromeUi: VoiceRecordingChromeUi? = null,
    useMinimumScriptHeight: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgRegular)
            .padding(vertical = PrezelTheme.spacing.V4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!recordingState.isCompleted || isScriptExpanded) {
            VoiceRecordingScriptHeader(
                isScriptExpanded = isScriptExpanded,
                onToggleScriptExpanded = onToggleScriptExpanded,
            )
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V4))
        }

        VoiceRecordingScriptBody(
            script = script,
            modifier = if (useMinimumScriptHeight) {
                Modifier.heightIn(min = 94.dp)
            } else {
                Modifier.weight(1f)
            },
        )

        if (recordingState !is AudioSessionState.Idle) {
            VoiceRecordingStatusArea(
                recordingState = recordingState,
                recordingVolumes = recordingVolumes,
                voiceChromeUi = voiceChromeUi,
                onClickRecordingControl = onClickRecordingControl,
            )
        }
    }
}

@Composable
private fun VoiceRecordingScriptHeader(
    isScriptExpanded: Boolean,
    onToggleScriptExpanded: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = PrezelTheme.spacing.V20),
    ) {
        Text(
            text = stringResource(R.string.feature_analysis_impl_voice_recording_script_label),
            color = PrezelTheme.colors.textMedium,
            style = PrezelTheme.typography.body3Medium,
            modifier = Modifier.align(Alignment.CenterStart),
        )

        ScriptZoomButton(
            isScriptExpanded = isScriptExpanded,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = PrezelTheme.spacing.V12)
                .size(48.dp),
            onClick = onToggleScriptExpanded,
        )
    }
}

@Composable
private fun VoiceRecordingScriptBody(
    script: String,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val showTopGradient by remember {
        derivedStateOf { scrollState.value > 0 }
    }
    val showBottomGradient by remember {
        derivedStateOf { scrollState.value < scrollState.maxValue }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V20),
    ) {
        val backgroundColor = PrezelTheme.colors.bgRegular

        VoiceRecordingScriptText(
            script = script,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
        )

        if (showTopGradient) {
            VoiceRecordingScriptGradient(
                modifier = Modifier.align(Alignment.TopCenter),
                brush = Brush.verticalGradient(
                    colors = listOf(backgroundColor, Color.Transparent),
                ),
            )
        }

        if (showBottomGradient) {
            VoiceRecordingScriptGradient(
                modifier = Modifier.align(Alignment.BottomCenter),
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, backgroundColor),
                ),
            )
        }
    }
}

@Composable
private fun VoiceRecordingScriptText(
    script: String,
    modifier: Modifier = Modifier,
) {
    val isScriptBlank = script.isBlank()

    Text(
        text = if (isScriptBlank) {
            stringResource(R.string.feature_analysis_impl_voice_recording_no_script)
        } else {
            script
        },
        color = if (isScriptBlank) {
            PrezelTheme.colors.textRegular
        } else {
            PrezelTheme.colors.textLarge
        },
        style = PrezelTheme.typography.body2Regular,
        modifier = modifier,
    )
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
    recordingVolumes: ImmutableList<Float>,
    voiceChromeUi: VoiceRecordingChromeUi?,
    onClickRecordingControl: () -> Unit,
) {
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
    RecordingWaveform(
        recordingState = recordingState,
        recordingVolumes = recordingVolumes,
        voiceChromeUi = voiceChromeUi,
        modifier = Modifier.fillMaxWidth(),
    )
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
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
}

@Composable
private fun ScriptZoomButton(
    isScriptExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(
                if (isScriptExpanded) {
                    PrezelIcons.ZoomOut
                } else {
                    PrezelIcons.ZoomIn
                },
            ),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = PrezelTheme.colors.iconDisabled,
        )
    }
}

@Composable
private fun RecordingWaveform(
    recordingState: AudioSessionState,
    recordingVolumes: ImmutableList<Float>,
    voiceChromeUi: VoiceRecordingChromeUi?,
    modifier: Modifier = Modifier,
) {
    val playbackProgress = recordingState.playbackProgress()
    val visibleVolumes = if (voiceChromeUi?.hideWaveform == true) {
        persistentListOf()
    } else {
        recordingState.visibleRecordingVolumes(
            recordingVolumes = recordingVolumes,
            playbackProgress = playbackProgress,
        )
    }

    PrezelVoiceChromeWave(
        status = voiceChromeUi?.status ?: recordingState.toVoiceChromeStatus(),
        volumes = visibleVolumes,
        showBaseline = false,
        modifier = modifier,
    )
}

@Composable
private fun AudioSessionState.playbackProgress(): Float {
    if (this !is AudioSessionState.Playing) return playbackProgress

    return key(source, durationSeconds) {
        val animatedPlaybackProgress by animateFloatAsState(
            targetValue = playbackProgress,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
            label = "RecordingWaveformPlaybackProgress",
        )

        animatedPlaybackProgress
    }
}

private fun AudioSessionState.visibleRecordingVolumes(
    recordingVolumes: ImmutableList<Float>,
    playbackProgress: Float,
): ImmutableList<Float> =
    when (this) {
        is AudioSessionState.Playing -> {
            if (durationSeconds <= 0 || recordingVolumes.isEmpty()) {
                recordingVolumes
            } else {
                val visibleCount = (playbackProgress * recordingVolumes.size)
                    .toInt()
                    .coerceIn(1, recordingVolumes.size)
                recordingVolumes.take(visibleCount).toImmutableList()
            }
        }

        AudioSessionState.Idle,
        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        is AudioSessionState.ReadyToPlay,
        -> recordingVolumes
    }

private val AudioSessionState.playbackProgress: Float
    get() = when (this) {
        is AudioSessionState.Playing -> {
            if (durationSeconds <= 0) 0f else positionSeconds.toFloat() / durationSeconds
        }

        AudioSessionState.Idle,
        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> 0f

        is AudioSessionState.ReadyToPlay -> 1f
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
            withStyle(SpanStyle(color = PrezelTheme.colors.textDisabled)) {
                append(currentSeconds.toTimerText())
                append("/")
            }
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
private fun RecordingRoundIconButton(
    iconResId: Int,
    iconColor: Color,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            isRounded = true,
            type = ButtonType.FILLED,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.SECONDARY,
            contentColor = iconColor,
            backgroundColor = PrezelTheme.colors.bgLarge,
            iconSize = 24.dp,
        ),
        onClick = onClick,
    )
}

@BasicPreview
@Composable
private fun VoiceRecordingContentIdlePreview() {
    VoiceRecordingContentPreview(recordingState = AudioSessionState.Idle)
}

@BasicPreview
@Composable
private fun VoiceRecordingContentEmptyScriptPreview() {
    VoiceRecordingContentPreview(
        recordingState = AudioSessionState.Idle,
        script = "",
        useMinimumScriptHeight = true,
    )
}

@BasicPreview
@Composable
private fun VoiceRecordingContentRecordingPreview() {
    VoiceRecordingContentPreview(
        recordingState = AudioSessionState.Recording(elapsedSeconds = 12),
    )
}

@BasicPreview
@Composable
private fun VoiceRecordingContentCompletedPreview() {
    VoiceRecordingContentPreview(
        recordingState = AudioSessionState.ReadyToPlay(
            source = AudioSource.RecordedFile(filePath = "preview.m4a"),
            durationSeconds = 75,
        ),
    )
}

@Composable
private fun VoiceRecordingContentPreview(
    recordingState: AudioSessionState,
    script: String = "한 번쯤 발표하면서 긴장하신 경험 있으시죠. 오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
    useMinimumScriptHeight: Boolean = false,
) {
    PrezelTheme {
        VoiceRecordingContent(
            script = script,
            recordingState = recordingState,
            recordingVolumes = persistentListOf(0.2f, 0.45f, 0.7f, 0.35f, 0.8f, 0.55f),
            isScriptExpanded = false,
            onToggleScriptExpanded = {},
            onClickRecordingControl = {},
            modifier = if (useMinimumScriptHeight) Modifier.fillMaxWidth() else Modifier.fillMaxSize(),
            useMinimumScriptHeight = useMinimumScriptHeight,
        )
    }
}
