package com.team.prezel.feature.practice.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.practice.impl.component.PracticeRecordingContent
import com.team.prezel.feature.practice.impl.component.PracticeRecordingTopAppBar
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiState
import com.team.prezel.feature.practice.impl.model.PracticeRecordingUiMessage
import kotlinx.coroutines.launch

@Composable
internal fun PracticeRecordingScreen(
    onBack: () -> Unit,
    navigateToAnalysis: (recordingFilePath: String, referenceText: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeRecordingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val showMessage: (PracticeRecordingUiMessage) -> Unit = { message ->
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showPrezelSnackbar(message = resources.getString(message.resId))
        }
    }
    val onClickRecordingControl = rememberRecordAudioPermissionControlClickHandler(
        recordingState = uiState.recordingState,
        onClickRecordingControl = { viewModel.onIntent(PracticeRecordingUiIntent.ClickRecordingControl) },
        onPermissionDenied = { showMessage(PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_DENIED) },
        onPermissionPermanentlyDenied = {
            showMessage(PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_PERMANENTLY_DENIED)
        },
    )

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PracticeRecordingUiEffect.ShowMessage -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(effect.message.resId))
                }
            }
        }
    }

    PracticeRecordingScreen(
        uiState = uiState,
        onClickRecordingControl = onClickRecordingControl,
        onClickAnalyze = {
            val recordingFilePath = uiState.recordingFilePath ?: return@PracticeRecordingScreen
            navigateToAnalysis(recordingFilePath, uiState.practiceScript)
        },
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun PracticeRecordingScreen(
    uiState: PracticeRecordingUiState,
    onClickRecordingControl: () -> Unit,
    onClickAnalyze: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(
        onBack = onBack,
    )

    PracticeRecordingReadyScreen(
        uiState = uiState,
        onClickRecordingControl = onClickRecordingControl,
        onClickAnalyze = onClickAnalyze,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun PracticeRecordingReadyScreen(
    uiState: PracticeRecordingUiState,
    onClickRecordingControl: () -> Unit,
    onClickAnalyze: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PracticeRecordingTopAppBar(onBack = onBack)

        PracticeRecordingContent(
            practiceScript = uiState.practiceScript,
            currentSeconds = uiState.currentSeconds,
            totalSeconds = uiState.totalSeconds,
            recordingState = uiState.recordingState,
            onClickRecordingControl = onClickRecordingControl,
            modifier = Modifier.weight(1f),
        )

        PrezelButtonArea(
            mainButton = { buttonModifier ->
                PrezelButton(
                    text = stringResource(R.string.feature_practice_impl_practice_recording_analyze),
                    modifier = buttonModifier,
                    enabled = uiState.analyzeEnabled,
                    onClick = onClickAnalyze,
                )
            },
        )
    }
}

private val PracticeRecordingUiMessage.resId: Int
    get() = when (this) {
        PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED -> R.string.feature_practice_impl_practice_recording_fetch_script_failed
        PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_DENIED -> R.string.feature_practice_impl_practice_recording_permission_denied
        PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_PERMANENTLY_DENIED ->
            R.string.feature_practice_impl_practice_recording_permission_permanently_denied

        PracticeRecordingUiMessage.RECORDING_START_FAILED -> R.string.feature_practice_impl_practice_recording_failed
        PracticeRecordingUiMessage.RECORDING_STOP_FAILED -> R.string.feature_practice_impl_practice_recording_stop_failed
        PracticeRecordingUiMessage.PLAYBACK_START_FAILED -> R.string.feature_practice_impl_practice_recording_playback_failed
    }

@BasicPreview
@Composable
private fun PracticeRecordingScreenIdlePreview() {
    PrezelTheme {
        PracticeRecordingScreenPreviewContent(uiState = PracticeRecordingUiState())
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingScreenRecordingPreview() {
    PrezelTheme {
        PracticeRecordingScreenPreviewContent(
            uiState = PracticeRecordingUiState(
                recordingState = AudioSessionState.Recording(
                    elapsedSeconds = 12,
                ),
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingScreenRecordedPreview() {
    PrezelTheme {
        PracticeRecordingScreenPreviewContent(
            uiState = PracticeRecordingUiState(
                recordingState = AudioSessionState.ReadyToPlay(
                    source = AudioSource.RecordedFile(filePath = ""),
                    durationSeconds = 32,
                ),
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingScreenPlayingPreview() {
    PrezelTheme {
        PracticeRecordingScreenPreviewContent(
            uiState = PracticeRecordingUiState(
                recordingState = AudioSessionState.Playing(
                    source = AudioSource.RecordedFile(filePath = ""),
                    positionSeconds = 12,
                    durationSeconds = 32,
                ),
            ),
        )
    }
}

@Composable
private fun PracticeRecordingScreenPreviewContent(uiState: PracticeRecordingUiState) {
    PracticeRecordingScreen(
        uiState = uiState.copy(
            practiceScript = "내가 그린 기린 그림은 잘 그린 기린 그림이고,\n네가 그린 기린 그림은 잘못 그린 기린 그림이다.",
        ),
        onClickRecordingControl = {},
        onClickAnalyze = {},
        onBack = {},
    )
}
