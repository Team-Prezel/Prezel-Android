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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.practice.component.PracticeAnalysisSpeed
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingAnalysisErrorPage
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingAnalysisLoadingPage
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingAnalysisSuccessPage
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingButtonArea
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingContent
import com.team.prezel.feature.home.impl.practice.component.PracticeRecordingControlState
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingPhase
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingUiState

@Composable
internal fun PracticeRecordingScreen(
    onBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeRecordingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hasRecordAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED,
        )
    }
    val scripts = stringArrayResource(R.array.feature_home_impl_practice_recording_scripts)
    val practiceScript = remember { scripts.random() }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        hasRecordAudioPermission = isGranted
        if (isGranted) viewModel.onIntent(PracticeRecordingUiIntent.ClickControl)
    }

    fun onClickRecordingControl() {
        when (uiState.phase) {
            PracticeRecordingPhase.IDLE -> {
                if (hasRecordAudioPermission) {
                    viewModel.onIntent(PracticeRecordingUiIntent.ClickControl)
                } else {
                    recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }

            PracticeRecordingPhase.RECORDING,
            PracticeRecordingPhase.RECORDED,
            PracticeRecordingPhase.PLAYING,
            -> viewModel.onIntent(PracticeRecordingUiIntent.ClickControl)
        }
    }

    PracticeRecordingScreen(
        uiState = uiState,
        practiceScript = practiceScript,
        onClickControl = ::onClickRecordingControl,
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

    if (uiState.analysisStatus != PracticeRecordingAnalysisStatus.Ready) {
        PracticeRecordingAnalysisScreen(
            analysisStatus = uiState.analysisStatus,
            onBack = onBack,
            onRetry = onClickAnalyze,
            onComplete = navigateToHome,
            modifier = modifier,
        )
        return
    }

    PracticeRecordingReadyScreen(
        uiState = uiState,
        practiceScript = practiceScript,
        onClickControl = onClickControl,
        onClickAnalyze = onClickAnalyze,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun PracticeRecordingAnalysisScreen(
    analysisStatus: PracticeRecordingAnalysisStatus,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (analysisStatus) {
        PracticeRecordingAnalysisStatus.Loading -> PracticeRecordingAnalysisLoadingPage(modifier = modifier)
        PracticeRecordingAnalysisStatus.Success -> PracticeRecordingAnalysisSuccessPage(
            pronunciationScore = 90,
            speed = PracticeAnalysisSpeed.ADEQUATE,
            onBack = onBack,
            onComplete = onComplete,
            modifier = modifier,
        )

        is PracticeRecordingAnalysisStatus.Error -> PracticeRecordingAnalysisErrorPage(
            errorType = analysisStatus.type,
            onRetry = onRetry,
            modifier = modifier,
        )

        PracticeRecordingAnalysisStatus.Ready -> Unit
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
            controlState = uiState.phase.toControlState(),
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

private fun PracticeRecordingPhase.toControlState(): PracticeRecordingControlState =
    when (this) {
        PracticeRecordingPhase.IDLE -> PracticeRecordingControlState.READY_TO_RECORD
        PracticeRecordingPhase.RECORDING -> PracticeRecordingControlState.RECORDING
        PracticeRecordingPhase.RECORDED -> PracticeRecordingControlState.READY_TO_PLAY
        PracticeRecordingPhase.PLAYING -> PracticeRecordingControlState.PLAYING
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
                phase = PracticeRecordingPhase.RECORDING,
                recordingSeconds = 12,
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
                phase = PracticeRecordingPhase.RECORDED,
                playbackSeconds = 0,
                recordedDurationSeconds = 32,
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
                phase = PracticeRecordingPhase.PLAYING,
                playbackSeconds = 12,
                recordedDurationSeconds = 32,
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
