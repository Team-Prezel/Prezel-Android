package com.team.prezel.core.ui.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.feedback.progress.PrezelProgressBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.R
import com.team.prezel.core.ui.util.noRippleClickable
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Immutable
sealed interface FileUploaderState {
    @Immutable
    sealed interface Script : FileUploaderState {
        @Immutable
        data object Loading : Script

        @Immutable
        data object Uploaded : Script
    }

    @Immutable
    sealed interface Audio : FileUploaderState {
        @Immutable
        data object Loading : Audio

        @Immutable
        data object Paused : Audio

        @Immutable
        data object Playing : Audio
    }
}

@Composable
fun FileUploader(
    fileName: String,
    state: FileUploaderState,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0) progress: Float = 0f,
    currentTimeText: String = "00:00",
    durationTimeText: String = "00:00",
    onCancelClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSeek: (Float) -> Unit = {},
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    val playing = state is FileUploaderState.Audio.Playing
    val showAudioControl = state is FileUploaderState.Audio && state !is FileUploaderState.Audio.Loading
    val isLoading = when (state) {
        FileUploaderState.Script.Loading,
        FileUploaderState.Audio.Loading -> true
        FileUploaderState.Script.Uploaded,
        FileUploaderState.Audio.Paused,
        FileUploaderState.Audio.Playing -> false
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 68.dp)
            .clip(PrezelTheme.shapes.V8)
            .background(color = PrezelTheme.colors.bgRegular)
            .border(
                width = PrezelTheme.stroke.V1,
                color = PrezelTheme.colors.borderRegular,
                shape = PrezelTheme.shapes.V8,
            ).padding(
                horizontal = PrezelTheme.spacing.V16,
                vertical = PrezelTheme.spacing.V12,
            ),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FileUploaderContent(
            fileName = fileName,
            progress = coercedProgress,
            currentTimeText = currentTimeText,
            durationTimeText = durationTimeText,
            showAudioControl = showAudioControl,
            isLoading = isLoading,
            playing = playing,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            onSeek = onSeek,
            modifier = Modifier.weight(1f),
        )

        CancelButton(onClick = onCancelClick)
    }
}

@Composable
private fun FileUploaderContent(
    fileName: String,
    progress: Float,
    currentTimeText: String,
    durationTimeText: String,
    showAudioControl: Boolean,
    isLoading: Boolean,
    playing: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        if (showAudioControl) {
            AudioFileHeader(
                fileName = fileName,
                playing = playing,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
            )
            AudioProgressRow(
                currentTimeText = currentTimeText,
                durationTimeText = durationTimeText,
                progress = progress,
                playing = playing,
                onSeek = onSeek,
            )
        } else {
            FileNameText(fileName = fileName)

            if (isLoading) {
                UploadProgressRow(progress = progress)
            }
        }
    }
}

@Composable
private fun FileNameText(
    fileName: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = fileName,
        modifier = modifier,
        style = PrezelTheme.typography.body3Medium,
        color = PrezelTheme.colors.textMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun UploadProgressRow(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val progressDescription = stringResource(R.string.core_ui_file_uploader_progress_desc)

        PrezelProgressBar(
            progress = progress,
            modifier = Modifier
                .weight(1f)
                .semantics {
                    contentDescription = progressDescription
                },
        )

        Text(
            text = stringResource(
                id = R.string.core_ui_file_uploader_progress_percent,
                progress.toPercentValue(),
            ),
            style = PrezelTheme.typography.caption2Regular,
            color = PrezelTheme.colors.textSmall,
        )
    }
}

@Composable
private fun AudioFileHeader(
    fileName: String,
    playing: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(if (playing) PrezelIcons.Pause else PrezelIcons.Play),
            contentDescription = stringResource(
                if (playing) {
                    R.string.core_ui_file_uploader_pause_desc
                } else {
                    R.string.core_ui_file_uploader_play_desc
                },
            ),
            tint = PrezelTheme.colors.iconRegular,
            modifier = Modifier
                .size(20.dp)
                .noRippleClickable(onClick = if (playing) onPauseClick else onPlayClick),
        )
        FileNameText(fileName = fileName)
    }
}

@Composable
private fun AudioProgressRow(
    currentTimeText: String,
    durationTimeText: String,
    progress: Float,
    playing: Boolean,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AudioTimeText(
            currentTimeText = currentTimeText,
            durationTimeText = durationTimeText,
            playing = playing,
        )

        AudioSeekBar(
            progress = progress,
            onSeek = onSeek,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AudioTimeText(
    currentTimeText: String,
    durationTimeText: String,
    playing: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = if (playing) PrezelTheme.colors.interactiveRegular else PrezelTheme.colors.textSmall,
                ),
            ) {
                append(currentTimeText)
            }
            withStyle(style = SpanStyle(color = PrezelTheme.colors.textSmall)) {
                append(stringResource(R.string.core_ui_file_uploader_time_separator))
                append(durationTimeText)
            }
        },
        modifier = modifier,
        style = PrezelTheme.typography.caption2Regular,
    )
}

@Composable
private fun AudioSeekBar(
    progress: Float,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var widthPx by remember { mutableIntStateOf(0) }
    val currentOnSeek by rememberUpdatedState(onSeek)
    val seekDescription = stringResource(R.string.core_ui_file_uploader_seek_desc)
    val coercedProgress = progress.coerceIn(0f, 1f)

    fun seekTo(offsetX: Float) {
        if (widthPx > 0) currentOnSeek((offsetX / widthPx).coerceIn(0f, 1f))
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
            .onSizeChanged { widthPx = it.width }
            .pointerInput(Unit) {
                detectTapGestures { offset -> seekTo(offset.x) }
            }.pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    seekTo(change.position.x)
                    change.consume()
                }
            }.semantics {
                contentDescription = seekDescription
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedProgress,
                    range = 0f..1f,
                )
                setProgress { targetProgress ->
                    currentOnSeek(targetProgress.coerceIn(0f, 1f))
                    true
                }
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        AudioSeekTrack(progress = coercedProgress)
        AudioSeekHandle(progress = coercedProgress)
    }
}

@Composable
private fun AudioSeekTrack(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.bgLarge),
    )
    Box(
        modifier = Modifier
            .fillMaxWidth(progress)
            .height(4.dp)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.interactiveRegular),
    )
}

@Composable
private fun BoxWithConstraintsScope.AudioSeekHandle(progress: Float) {
    val handleSize = 12.dp

    Box(
        modifier = Modifier
            .offset {
                val handleRadiusPx = handleSize.toPx() / 2
                val handleX = ((maxWidth.toPx() * progress) - handleRadiusPx).roundToInt()
                IntOffset(x = handleX, y = 0)
            }.size(handleSize)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.interactiveRegular),
    )
}

@Composable
private fun CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(PrezelTheme.shapes.V1000)
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(PrezelIcons.CancelCircleFilled),
            contentDescription = stringResource(R.string.core_ui_file_uploader_cancel_desc),
            tint = PrezelTheme.colors.iconRegular,
            modifier = Modifier.size(24.dp),
        )
    }
}

private fun Float.toPercentValue(): Int = (coerceIn(0f, 1f) * 100).roundToInt()

@BasicPreview
@Composable
private fun FileUploaderPreview() {
    PrezelTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            modifier = Modifier.padding(PrezelTheme.spacing.V16),
        ) {
            FileUploader(
                fileName = "title.txt",
                state = FileUploaderState.Script.Loading,
                progress = 0.42f,
                modifier = Modifier.width(420.dp),
            )
            FileUploader(
                fileName = "title.txt",
                state = FileUploaderState.Script.Uploaded,
                modifier = Modifier.width(420.dp),
            )
            FileUploader(
                fileName = "title.mp3",
                state = FileUploaderState.Audio.Paused,
                modifier = Modifier.width(420.dp),
            )
            FileUploader(
                fileName = "title.mp3",
                state = FileUploaderState.Audio.Playing,
                progress = 0.275f,
                currentTimeText = "00:11",
                durationTimeText = "00:40",
                modifier = Modifier.width(420.dp),
            )
        }
    }
}

@BasicPreview
@Composable
private fun FileUploaderInteractivePreview() {
    var playing by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    val durationSeconds = 40

    LaunchedEffect(playing) {
        while (playing && elapsedSeconds < durationSeconds) {
            delay(1_000L)
            elapsedSeconds = (elapsedSeconds + 1).coerceAtMost(durationSeconds)
        }

        if (elapsedSeconds == durationSeconds) {
            playing = false
        }
    }

    PrezelTheme {
        Box(modifier = Modifier.padding(PrezelTheme.spacing.V16)) {
            FileUploader(
                fileName = "title.mp3",
                state = if (playing) {
                    FileUploaderState.Audio.Playing
                } else {
                    FileUploaderState.Audio.Paused
                },
                progress = elapsedSeconds.toFloat() / durationSeconds,
                currentTimeText = elapsedSeconds.toPreviewTimeText(),
                durationTimeText = durationSeconds.toPreviewTimeText(),
                onPlayClick = { playing = true },
                onPauseClick = { playing = false },
                onSeek = { seekProgress ->
                    elapsedSeconds = (durationSeconds * seekProgress).roundToInt()
                },
                modifier = Modifier.width(420.dp),
            )
        }
    }
}

private fun Int.toPreviewTimeText(): String {
    val minutes = this / 60
    val seconds = this % 60

    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
