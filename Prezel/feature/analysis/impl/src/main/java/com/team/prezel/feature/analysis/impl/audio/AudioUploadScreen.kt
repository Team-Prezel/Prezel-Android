package com.team.prezel.feature.analysis.impl.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.FileUploader
import com.team.prezel.core.ui.component.FileUploaderState
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.analysis.impl.R
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import com.team.prezel.feature.analysis.impl.component.AnalysisStepTitle
import com.team.prezel.feature.analysis.impl.component.toFileName
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private const val AUDIO_UPLOAD_PROGRESS_DURATION_MILLIS = 800
private const val AUDIO_PLAYBACK_PROGRESS_INTERVAL_MILLIS = 250L
private val AUDIO_FILE_MIME_TYPES = arrayOf(
    "audio/mpeg", // mp3
    "audio/mp4", // mp4, m4a
    "audio/x-m4a", // m4a
)
private const val AUDIO_UPLOAD_TAB_COUNT = 1

@Composable
internal fun AudioUploadScreen(
    uiState: AnalysisFlowUiState,
    onAudioFileSelected: (fileUri: String?, fileName: String?) -> Unit,
    onAnalyze: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var pendingAudioFileUri by remember { mutableStateOf<String?>(null) }
    val uploadProgress by animateFloatAsState(
        targetValue = if (pendingAudioFileUri != null) 1f else 0f,
        animationSpec = tween(
            durationMillis = AUDIO_UPLOAD_PROGRESS_DURATION_MILLIS,
            easing = LinearEasing,
        ),
        label = "audio-upload-progress",
    )

    val audioPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.toString()?.let { fileUri ->
            pendingAudioFileUri = fileUri
        }
    }

    LaunchedEffect(pendingAudioFileUri) {
        val fileUri = pendingAudioFileUri ?: return@LaunchedEffect
        delay(AUDIO_UPLOAD_PROGRESS_DURATION_MILLIS.toLong())
        onAudioFileSelected(fileUri, fileUri.toFileName(context))
    }

    LaunchedEffect(uiState.form.audioFileUri, pendingAudioFileUri) {
        if (pendingAudioFileUri != null && uiState.form.audioFileUri == pendingAudioFileUri) {
            pendingAudioFileUri = null
        }
    }

    AudioUploadScreen(
        form = uiState.form,
        pendingAudioFileUri = pendingAudioFileUri,
        uploadProgress = uploadProgress,
        progress = uiState.progress,
        buttonEnabled = uiState.canMoveNext,
        onAudioFileUploadClick = { audioPicker.launch(AUDIO_FILE_MIME_TYPES) },
        onAudioFileClear = {
            if (pendingAudioFileUri != null) {
                pendingAudioFileUri = null
            } else {
                onAudioFileSelected(null, null)
            }
        },
        onAnalyze = onAnalyze,
        onBack = onBack,
    )
}

@Composable
private fun AudioUploadScreen(
    form: AnalysisForm,
    pendingAudioFileUri: String?,
    uploadProgress: Float,
    progress: Float,
    buttonEnabled: Boolean,
    onAudioFileUploadClick: () -> Unit,
    onAudioFileClear: () -> Unit,
    onAnalyze: () -> Unit,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState { AUDIO_UPLOAD_TAB_COUNT }
    val tabs = persistentListOf(stringResource(R.string.feature_analysis_impl_upload_title))

    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_audio_title),
        progress = progress,
        buttonText = stringResource(R.string.feature_analysis_impl_analyze),
        buttonEnabled = buttonEnabled,
        onButtonClick = onAnalyze,
        onBack = onBack,
        contentScrollable = false,
        alwaysShowButtonAreaDivider = true,
    ) {
        AnalysisStepTitle(
            title = stringResource(R.string.feature_analysis_impl_audio_headline),
            description = stringResource(R.string.feature_analysis_impl_audio_description),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PrezelTabs(
            tabs = tabs,
            pagerState = pagerState,
            size = PrezelTabSize.SMALL,
            onClickTab = {},
        )

        AudioUploadContent(
            fileUri = pendingAudioFileUri ?: form.audioFileUri,
            uploadProgress = if (pendingAudioFileUri != null) uploadProgress else null,
            onUploadClick = onAudioFileUploadClick,
            onClear = onAudioFileClear,
        )
    }
}

@Composable
private fun ColumnScope.AudioUploadContent(
    fileUri: String?,
    uploadProgress: Float?,
    onUploadClick: () -> Unit,
    onClear: () -> Unit,
) {
    if (fileUri == null) {
        AudioUploadEmptyContent(
            onUploadClick = onUploadClick,
            modifier = Modifier.weight(1f),
        )
    } else {
        val context = LocalContext.current
        val playbackState = rememberAudioUploadPlaybackState(
            fileUri = fileUri,
            enabled = uploadProgress == null,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        FileUploader(
            fileName = remember(context, fileUri) { fileUri.toFileName(context) },
            state = when {
                uploadProgress != null -> FileUploaderState.Audio.Loading
                playbackState.playing -> FileUploaderState.Audio.Playing
                else -> FileUploaderState.Audio.Paused
            },
            progress = uploadProgress ?: audioPlaybackProgress(
                currentPositionMillis = playbackState.currentPositionMillis,
                durationMillis = playbackState.durationMillis,
            ),
            currentTimeText = playbackState.currentPositionMillis.toAudioPlaybackTimeText(),
            durationTimeText = playbackState.durationMillis.toAudioPlaybackTimeText(),
            onCancelClick = onClear,
            onPlayClick = playbackState::play,
            onPauseClick = playbackState::pause,
            onSeek = playbackState::seekToProgress,
        )
    }
}

@Composable
private fun rememberAudioUploadPlaybackState(
    fileUri: String,
    enabled: Boolean,
): AudioUploadPlaybackState {
    val context = LocalContext.current
    val state = remember(context, fileUri) {
        AudioUploadPlaybackState(
            context = context.applicationContext,
            fileUri = fileUri,
        )
    }

    LaunchedEffect(state.playing) {
        while (state.playing) {
            delay(AUDIO_PLAYBACK_PROGRESS_INTERVAL_MILLIS)
            state.syncPosition()
        }
    }

    LaunchedEffect(enabled) {
        if (!enabled) state.release()
    }

    DisposableEffect(state) {
        onDispose {
            state.release()
        }
    }

    return state
}

private class AudioUploadPlaybackState(
    private val context: Context,
    private val fileUri: String,
) {
    private var player: MediaPlayer? = null

    var playing by mutableStateOf(false)
        private set

    var currentPositionMillis by mutableIntStateOf(0)
        private set

    var durationMillis by mutableIntStateOf(0)
        private set

    fun play() {
        val mediaPlayer = player ?: preparePlayer() ?: return
        runCatching {
            mediaPlayer.start()
            playing = true
            syncPosition()
        }.onFailure {
            release()
        }
    }

    fun pause() {
        player?.runCatching {
            if (isPlaying) pause()
        }
        syncPosition()
        playing = false
    }

    fun seekToProgress(progress: Float) {
        val mediaPlayer = player ?: preparePlayer() ?: return
        val targetPositionMillis = (durationMillis * progress.coerceIn(0f, 1f)).roundToInt()

        runCatching {
            mediaPlayer.seekTo(targetPositionMillis)
            currentPositionMillis = targetPositionMillis
        }.onFailure {
            release()
        }
    }

    fun syncPosition() {
        val mediaPlayer = player ?: return
        currentPositionMillis = mediaPlayer.currentPosition.coerceAtLeast(0)
        durationMillis = mediaPlayer.duration.coerceAtLeast(0)
    }

    fun release() {
        player?.release()
        player = null
        playing = false
        currentPositionMillis = 0
        durationMillis = 0
    }

    private fun preparePlayer(): MediaPlayer? =
        runCatching {
            MediaPlayer.create(context, fileUri.toUri())?.apply {
                durationMillis = duration.coerceAtLeast(0)
                setOnCompletionListener {
                    currentPositionMillis = durationMillis
                    playing = false
                }
            }
        }.getOrNull()
            ?.also { player = it }
}

internal fun Int.toAudioPlaybackTimeText(): String {
    val totalSeconds = coerceAtLeast(0) / 1_000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

internal fun audioPlaybackProgress(
    currentPositionMillis: Int,
    durationMillis: Int,
): Float {
    if (durationMillis <= 0) return 0f
    return (currentPositionMillis.toFloat() / durationMillis).coerceIn(0f, 1f)
}

@Composable
private fun AudioUploadEmptyContent(
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusView(
        title = stringResource(R.string.feature_analysis_impl_audio_file_placeholder),
        description = stringResource(R.string.feature_analysis_impl_audio_file_format),
        modifier = modifier.fillMaxWidth(),
        visual = {
            Image(
                painter = painterResource(R.drawable.feature_analysis_impl_no_voice),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        },
        action = {
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_audio_upload_button),
                iconResId = PrezelIcons.Plus,
                type = ButtonType.OUTLINED,
                size = ButtonSize.SMALL,
                isRounded = true,
                onClick = onUploadClick,
            )
        },
    )
}

@BasicPreview
@Composable
private fun AudioUploadScreenPreview() {
    PrezelTheme {
        AudioUploadScreen(
            uiState = AnalysisFlowUiState(step = AnalysisFlowStep.AUDIO_UPLOAD),
            onAudioFileSelected = { _, _ -> },
            onAnalyze = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun AudioUploadScreenProgressPreview() {
    PrezelTheme {
        AudioUploadScreen(
            form = AnalysisForm(),
            pendingAudioFileUri = "content://prezel/sample.m4a",
            uploadProgress = 0.5f,
            progress = AnalysisFlowUiState(step = AnalysisFlowStep.AUDIO_UPLOAD).progress,
            buttonEnabled = false,
            onAudioFileUploadClick = {},
            onAudioFileClear = {},
            onAnalyze = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun AudioUploadScreenSelectedPreview() {
    PrezelTheme {
        AudioUploadScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.AUDIO_UPLOAD,
                form = AnalysisForm(audioFileUri = "content://prezel/sample.m4a"),
            ),
            onAudioFileSelected = { _, _ -> },
            onAnalyze = {},
            onBack = {},
        )
    }
}
