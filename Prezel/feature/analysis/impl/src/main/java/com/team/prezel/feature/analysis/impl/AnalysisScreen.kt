package com.team.prezel.feature.analysis.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.analysis.impl.audio.AudioUploadScreen
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiEffect
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage
import com.team.prezel.feature.analysis.impl.result.AnalysisLoadingScreen
import com.team.prezel.feature.analysis.impl.result.AnalysisReportScreen
import com.team.prezel.feature.analysis.impl.result.FileRecognitionFailedScreen
import com.team.prezel.feature.analysis.impl.result.ScriptFileRecognitionFailedScreen
import com.team.prezel.feature.analysis.impl.schedule.PresentationScheduleScreen
import com.team.prezel.feature.analysis.impl.script.ScriptInputScreen
import com.team.prezel.feature.analysis.impl.situation.PresentationSituationScreen

@Composable
internal fun AnalysisScreen(
    onBack: () -> Unit,
    viewModel: AnalysisFlowViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AnalysisFlowUiEffect.NavigateBack -> onBack()
                is AnalysisFlowUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        AnalysisUiMessage.AUTH_EXPIRED -> R.string.feature_analysis_impl_error_auth_expired
                        AnalysisUiMessage.ANALYSIS_FAILED -> R.string.feature_analysis_impl_error_analysis_failed
                        AnalysisUiMessage.NETWORK_FAILED -> R.string.feature_analysis_impl_error_network_failed
                        AnalysisUiMessage.UNKNOWN_FAILED -> R.string.feature_analysis_impl_error_unknown_failed
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    AnalysisScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun AnalysisScreen(
    uiState: AnalysisFlowUiState,
    onIntent: (AnalysisFlowUiIntent) -> Unit,
) {
    when (uiState.step) {
        AnalysisFlowStep.PRESENTATION_SCHEDULE -> PresentationScheduleScreen(
            uiState = uiState,
            onTitleChange = { onIntent(AnalysisFlowUiIntent.UpdatePresentationTitle(it)) },
            onDateChange = { onIntent(AnalysisFlowUiIntent.UpdatePresentationDate(it)) },
            onNext = { onIntent(AnalysisFlowUiIntent.Next) },
            onBack = { onIntent(AnalysisFlowUiIntent.Back) },
        )

        AnalysisFlowStep.PRESENTATION_SITUATION -> PresentationSituationScreen(
            uiState = uiState,
            onSelectCategory = { onIntent(AnalysisFlowUiIntent.selectSituationOption(it)) },
            onSelectPurpose = { onIntent(AnalysisFlowUiIntent.selectSituationOption(it)) },
            onSelectStyle = { onIntent(AnalysisFlowUiIntent.selectSituationOption(it)) },
            onSelectAudience = { onIntent(AnalysisFlowUiIntent.selectSituationOption(it)) },
            onNext = { onIntent(AnalysisFlowUiIntent.Next) },
            onBack = { onIntent(AnalysisFlowUiIntent.Back) },
        )

        AnalysisFlowStep.SCRIPT_INPUT -> ScriptInputScreen(
            uiState = uiState,
            onSelectInputType = { onIntent(AnalysisFlowUiIntent.SelectScriptInputType(it)) },
            onScriptChange = { onIntent(AnalysisFlowUiIntent.UpdateScript(it)) },
            onScriptFileSelected = { onIntent(AnalysisFlowUiIntent.SelectScriptFile(it)) },
            onNext = { onIntent(AnalysisFlowUiIntent.Next) },
            onSkip = { onIntent(AnalysisFlowUiIntent.SkipScript) },
            onBack = { onIntent(AnalysisFlowUiIntent.Back) },
        )

        AnalysisFlowStep.AUDIO_UPLOAD -> AudioUploadScreen(
            uiState = uiState,
            onAudioFileSelected = { onIntent(AnalysisFlowUiIntent.SelectAudioFile(it)) },
            onAnalyze = { onIntent(AnalysisFlowUiIntent.Next) },
            onBack = { onIntent(AnalysisFlowUiIntent.Back) },
        )

        AnalysisFlowStep.ANALYZING -> AnalysisLoadingScreen()

        AnalysisFlowStep.REPORT -> AnalysisReportScreen()

        AnalysisFlowStep.FILE_RECOGNITION_FAILED -> FileRecognitionFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.AUDIO)) },
        )

        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED -> ScriptFileRecognitionFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.SCRIPT)) },
        )
    }
}
