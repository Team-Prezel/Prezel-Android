package com.team.prezel.feature.analysis.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiEffect
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisSituationOption
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType

@Composable
internal fun AnalysisScreen(
    onBack: () -> Unit,
    viewModel: AnalysisFlowViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AnalysisFlowUiEffect.NavigateBack -> onBack()
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
            onSelectCategory = { onIntent(it.toSituationIntent()) },
            onSelectPurpose = { onIntent(it.toSituationIntent()) },
            onSelectStyle = { onIntent(it.toSituationIntent()) },
            onSelectAudience = { onIntent(it.toSituationIntent()) },
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

        AnalysisFlowStep.ANALYZING -> AnalysisLoadingScreen(
            onFinished = { onIntent(AnalysisFlowUiIntent.Next) },
        )

        AnalysisFlowStep.REPORT -> AnalysisReportScreen()

        AnalysisFlowStep.FILE_RECOGNITION_FAILED -> FileRecognitionFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.AUDIO)) },
        )

        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED -> ScriptFileRecognitionFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.SCRIPT)) },
        )
    }
}

private fun com.team.prezel.core.model.presentation.Category.toSituationIntent(): AnalysisFlowUiIntent =
    AnalysisFlowUiIntent.SelectSituationOption(AnalysisSituationOption.CategoryOption(this))

private fun com.team.prezel.core.model.presentation.Purpose.toSituationIntent(): AnalysisFlowUiIntent =
    AnalysisFlowUiIntent.SelectSituationOption(AnalysisSituationOption.PurposeOption(this))

private fun com.team.prezel.core.model.presentation.Style.toSituationIntent(): AnalysisFlowUiIntent =
    AnalysisFlowUiIntent.SelectSituationOption(AnalysisSituationOption.StyleOption(this))

private fun com.team.prezel.core.model.presentation.Audience.toSituationIntent(): AnalysisFlowUiIntent =
    AnalysisFlowUiIntent.SelectSituationOption(AnalysisSituationOption.AudienceOption(this))
