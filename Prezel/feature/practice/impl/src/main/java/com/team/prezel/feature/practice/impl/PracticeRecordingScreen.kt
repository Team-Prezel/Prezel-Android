package com.team.prezel.feature.practice.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.team.prezel.feature.practice.impl.model.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.practice.impl.model.PracticeRecordingUiMessage
import com.team.prezel.feature.practice.impl.result.PracticeRecordingResultScreen

@Composable
internal fun PracticeRecordingScreen(
    onBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeRecordingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val onStartRecording = rememberRecordAudioPermissionControlClickHandler(
        recordingState = uiState.recordingState,
        onStartRecording = { viewModel.onIntent(PracticeRecordingUiIntent.StartRecording) },
        onPermissionDenied = { viewModel.onIntent(PracticeRecordingUiIntent.RecordAudioPermissionDenied) },
        onPermissionPermanentlyDenied = {
            viewModel.onIntent(PracticeRecordingUiIntent.RecordAudioPermissionPermanentlyDenied)
        },
    )

    LaunchedEffect(Unit) {
        viewModel.onIntent(PracticeRecordingUiIntent.LoadPracticeScript)
    }

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
        onStartRecording = onStartRecording,
        onStopRecording = { viewModel.onIntent(PracticeRecordingUiIntent.StopRecording) },
        onStartPlayback = { viewModel.onIntent(PracticeRecordingUiIntent.StartPlayback) },
        onStopPlayback = { viewModel.onIntent(PracticeRecordingUiIntent.StopPlayback) },
        onClickAnalyze = { viewModel.onIntent(PracticeRecordingUiIntent.AnalyzeClicked) },
        onRetryRecording = { viewModel.onIntent(PracticeRecordingUiIntent.RetryRecordingClicked) },
        onBack = onBack,
        navigateToHome = navigateToHome,
        modifier = modifier,
    )
}

@Composable
private fun PracticeRecordingScreen(
    uiState: PracticeRecordingUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onStartPlayback: () -> Unit,
    onStopPlayback: () -> Unit,
    onClickAnalyze: () -> Unit,
    onRetryRecording: () -> Unit,
    onBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(
        onBack = {
            if (uiState.analysisStatus == PracticeRecordingAnalysisStatus.Ready) {
                onBack()
            }
        },
    )

    when (uiState.analysisStatus) {
        PracticeRecordingAnalysisStatus.Ready -> PracticeRecordingReadyScreen(
            uiState = uiState,
            onStartRecording = onStartRecording,
            onStopRecording = onStopRecording,
            onStartPlayback = onStartPlayback,
            onStopPlayback = onStopPlayback,
            onClickAnalyze = onClickAnalyze,
            onBack = onBack,
            modifier = modifier,
        )

        else -> PracticeRecordingResultScreen(
            analysisStatus = uiState.analysisStatus,
            onRetry = onRetryRecording,
            onComplete = navigateToHome,
            modifier = modifier,
        )
    }
}

@Composable
private fun PracticeRecordingReadyScreen(
    uiState: PracticeRecordingUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onStartPlayback: () -> Unit,
    onStopPlayback: () -> Unit,
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
            onStartRecording = onStartRecording,
            onStopRecording = onStopRecording,
            onStartPlayback = onStartPlayback,
            onStopPlayback = onStopPlayback,
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
        onStartRecording = {},
        onStopRecording = {},
        onStartPlayback = {},
        onStopPlayback = {},
        onClickAnalyze = {},
        onRetryRecording = {},
        onBack = {},
        navigateToHome = {},
    )
}
