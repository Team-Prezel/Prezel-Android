package com.team.prezel.feature.home.impl.practice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.practice.analysis.PracticeRecordingAnalysisScreen
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingButtonArea
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingContent
import com.team.prezel.feature.home.impl.practice.component.toControlState
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingState
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingUiMessage

@Composable
internal fun PracticeRecordingScreen(
    onBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeRecordingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val practiceScript = rememberPracticeScript()
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val recordAudioPermissionState = rememberRecordAudioPermissionState(
        onPermissionGranted = { viewModel.onIntent(PracticeRecordingUiIntent.ClickControl) },
    )
    val onClickRecordingControl = rememberClickRecordingControlHandler(
        recordingState = uiState.recordingState,
        hasRecordAudioPermission = recordAudioPermissionState.isGranted,
        onRequestRecordAudioPermission = recordAudioPermissionState.request,
        onClickControl = { viewModel.onIntent(PracticeRecordingUiIntent.ClickControl) },
    )

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PracticeRecordingUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        PracticeRecordingUiMessage.RECORDING_START_FAILED -> R.string.feature_home_impl_practice_recording_failed
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    PracticeRecordingScreen(
        uiState = uiState,
        practiceScript = practiceScript,
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
    practiceScript: String,
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
            practiceScript = practiceScript,
            onClickControl = onClickControl,
            onClickAnalyze = onClickAnalyze,
            onBack = onBack,
            modifier = modifier,
        )

        else -> PracticeRecordingAnalysisScreen(
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
    practiceScript: String,
    onClickControl: () -> Unit,
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
            practiceScript = practiceScript,
            currentSeconds = uiState.currentSeconds,
            totalSeconds = uiState.totalSeconds,
            controlState = uiState.recordingState.toControlState(),
            onClickControl = onClickControl,
            modifier = Modifier.weight(1f),
        )
        PracticeRecordingButtonArea(enabled = uiState.analyzeEnabled, onClickAnalyze = onClickAnalyze)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PracticeRecordingTopAppBar(onBack: () -> Unit) {
    PrezelTopAppBar(
        title = { Text(text = stringResource(R.string.feature_home_impl_practice_recording_title)) },
        leadingIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(PrezelIcons.ArrowLeft),
                    contentDescription = stringResource(R.string.feature_home_impl_practice_recording_back),
                )
            }
        },
    )
}

@Composable
private fun rememberPracticeScript(): String {
    val scripts = stringArrayResource(R.array.feature_home_impl_practice_recording_scripts)
    return remember { scripts.random() }
}

@Composable
private fun rememberRecordAudioPermissionState(onPermissionGranted: () -> Unit): RecordAudioPermissionState {
    val context = LocalContext.current
    val currentOnPermissionGranted by rememberUpdatedState(onPermissionGranted)
    var hasRecordAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED,
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        hasRecordAudioPermission = isGranted
        if (isGranted) currentOnPermissionGranted()
    }

    return remember(hasRecordAudioPermission, launcher) {
        RecordAudioPermissionState(
            isGranted = hasRecordAudioPermission,
            request = { launcher.launch(Manifest.permission.RECORD_AUDIO) },
        )
    }
}

private data class RecordAudioPermissionState(
    val isGranted: Boolean,
    val request: () -> Unit,
)

@Composable
private fun rememberClickRecordingControlHandler(
    recordingState: PracticeRecordingState,
    hasRecordAudioPermission: Boolean,
    onRequestRecordAudioPermission: () -> Unit,
    onClickControl: () -> Unit,
): () -> Unit =
    remember(
        recordingState,
        hasRecordAudioPermission,
        onRequestRecordAudioPermission,
        onClickControl,
    ) {
        {
            when (recordingState) {
                PracticeRecordingState.Idle -> {
                    if (hasRecordAudioPermission) {
                        onClickControl()
                    } else {
                        onRequestRecordAudioPermission()
                    }
                }

                is PracticeRecordingState.Recording,
                is PracticeRecordingState.Recorded,
                is PracticeRecordingState.Playing,
                -> onClickControl()
            }
        }
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
        uiState = uiState,
        practiceScript = "내가 그린 기린 그림은 잘 그린 기린 그림이고,\n네가 그린 기린 그림은 잘못 그린 기린 그림이다.",
        onClickControl = {},
        onClickAnalyze = {},
        onBack = {},
        navigateToHome = {},
    )
}
