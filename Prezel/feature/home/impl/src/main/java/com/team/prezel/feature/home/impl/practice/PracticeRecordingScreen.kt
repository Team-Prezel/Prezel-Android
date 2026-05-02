package com.team.prezel.feature.home.impl.practice

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
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingContent
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingTopAppBar
import com.team.prezel.feature.home.impl.practice.component.toControlState
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingState
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingUiMessage
import com.team.prezel.feature.home.impl.practice.result.PracticeRecordingResultScreen
import kotlinx.coroutines.flow.collectLatest

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
    val onClickRecordingControl = rememberRecordAudioPermissionControlClickHandler(
        recordingState = uiState.recordingState,
        onClickControl = { viewModel.onIntent(PracticeRecordingUiIntent.ClickControl) },
        onPermissionDenied = { viewModel.onIntent(PracticeRecordingUiIntent.DenyRecordAudioPermission) },
        onPermissionPermanentlyDenied = {
            viewModel.onIntent(PracticeRecordingUiIntent.DenyRecordAudioPermissionPermanently)
        },
    )

    LaunchedEffect(Unit) {
        viewModel.onIntent(PracticeRecordingUiIntent.LoadPracticeScript)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is PracticeRecordingUiEffect.ShowMessage -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showPrezelSnackbar(
                        message = resources.getString(effect.message.resId),
                    )
                }
            }
        }
    }

    PracticeRecordingScreen(
        uiState = uiState,
        onClickControl = onClickRecordingControl,
        onClickAnalyze = { viewModel.onIntent(PracticeRecordingUiIntent.ClickAnalyze) },
        onBack = onBack,
        navigateToHome = navigateToHome,
        modifier = modifier,
    )
}

@Composable
private fun PracticeRecordingScreen(
    uiState: PracticeRecordingUiState,
    onClickControl: () -> Unit,
    onClickAnalyze: () -> Unit,
    onBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onBack)

    when (uiState.analysisStatus) {
        PracticeRecordingAnalysisStatus.Ready -> PracticeRecordingReadyScreen(
            uiState = uiState,
            onClickControl = onClickControl,
            onClickAnalyze = onClickAnalyze,
            onBack = onBack,
            modifier = modifier,
        )

        else -> PracticeRecordingResultScreen(
            analysisStatus = uiState.analysisStatus,
            onBack = onBack,
            onRetry = onClickAnalyze,
            onComplete = navigateToHome,
            modifier = modifier,
        )
    }
}

@Composable
private fun PracticeRecordingReadyScreen(
    uiState: PracticeRecordingUiState,
    onClickControl: () -> Unit,
    onClickAnalyze: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val analyzeLabel = stringResource(R.string.feature_home_impl_practice_recording_analyze)

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
            controlState = uiState.recordingState.toControlState(),
            onClickControl = onClickControl,
            modifier = Modifier.weight(1f),
        )

        PrezelButtonArea {
            MainButton(
                label = analyzeLabel,
                enabled = uiState.analyzeEnabled,
                onClick = onClickAnalyze,
            )
        }
    }
}

private val PracticeRecordingUiMessage.resId: Int
    get() = when (this) {
        PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED -> R.string.feature_home_impl_practice_recording_fetch_script_failed
        PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_DENIED -> R.string.feature_home_impl_practice_recording_permission_denied
        PracticeRecordingUiMessage.RECORD_AUDIO_PERMISSION_PERMANENTLY_DENIED ->
            R.string.feature_home_impl_practice_recording_permission_permanently_denied

        PracticeRecordingUiMessage.RECORDING_START_FAILED -> R.string.feature_home_impl_practice_recording_failed
        PracticeRecordingUiMessage.RECORDING_STOP_FAILED -> R.string.feature_home_impl_practice_recording_stop_failed
        PracticeRecordingUiMessage.PLAYBACK_START_FAILED -> R.string.feature_home_impl_practice_recording_playback_failed
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
                recordingState = PracticeRecordingState.Recording(
                    recordingSeconds = 12,
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
                recordingState = PracticeRecordingState.Recorded(
                    recordedDurationSeconds = 32,
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
                recordingState = PracticeRecordingState.Playing(
                    playbackSeconds = 12,
                    recordedDurationSeconds = 32,
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
        onClickControl = {},
        onClickAnalyze = {},
        onBack = {},
        navigateToHome = {},
    )
}
