package com.team.prezel.core.ui.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlin.math.roundToInt

@Immutable
enum class FileUploaderType {
    SCRIPT,
    AUDIO,
}

@Immutable
enum class FileUploaderState {
    LOADING,
    UPLOADED,
    PAUSED,
    PLAYING,
}

@Composable
fun FileUploader(
    fileName: String,
    type: FileUploaderType,
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
    val playing = state == FileUploaderState.PLAYING

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
            type = type,
            state = state,
            progress = coercedProgress,
            currentTimeText = currentTimeText,
            durationTimeText = durationTimeText,
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
    type: FileUploaderType,
    state: FileUploaderState,
    progress: Float,
    currentTimeText: String,
    durationTimeText: String,
    playing: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showAudioControl = type == FileUploaderType.AUDIO &&
        (state == FileUploaderState.PAUSED || state == FileUploaderState.PLAYING)

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

            if (state == FileUploaderState.LOADING) {
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
                .clickable(onClick = if (playing) onPauseClick else onPlayClick),
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
    Box(
        modifier = Modifier
            .offset {
                val handleX = ((maxWidth.toPx() - 14.dp.toPx()) * progress).roundToInt()
                IntOffset(x = handleX, y = 0)
            }.size(12.dp)
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
            .clickable(onClick = onClick),
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
                type = FileUploaderType.SCRIPT,
                state = FileUploaderState.LOADING,
                progress = 0.42f,
                modifier = Modifier.width(420.dp),
            )
            FileUploader(
                fileName = "title.txt",
                type = FileUploaderType.SCRIPT,
                state = FileUploaderState.UPLOADED,
                modifier = Modifier.width(420.dp),
            )
            FileUploader(
                fileName = "title.mp3",
                type = FileUploaderType.AUDIO,
                state = FileUploaderState.PAUSED,
                modifier = Modifier.width(420.dp),
            )
            FileUploader(
                fileName = "title.mp3",
                type = FileUploaderType.AUDIO,
                state = FileUploaderState.PLAYING,
                progress = 0.275f,
                currentTimeText = "00:11",
                durationTimeText = "00:40",
                modifier = Modifier.width(420.dp),
            )
        }
    }
}
