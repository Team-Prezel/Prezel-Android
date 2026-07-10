package com.team.prezel.feature.analysis.impl

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.common.event.EdgeToEdgeStatusBarStyle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.voice.VoiceChromeGradient
import com.team.prezel.core.designsystem.component.voice.VoiceChromeStatus
import com.team.prezel.core.ui.state.LocalSnackbarCoroutineScope
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.analysis.impl.audio.AudioUploadScreen
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiEffect
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage
import com.team.prezel.feature.analysis.impl.recording.VoiceRecordingChromeUi
import com.team.prezel.feature.analysis.impl.recording.VoiceRecordingScreen
import com.team.prezel.feature.analysis.impl.recording.VoiceRecordingStatusBarStyle
import com.team.prezel.feature.analysis.impl.recording.isCompleted
import com.team.prezel.feature.analysis.impl.recording.rememberAnalysisRecordAudioPermissionControlClickHandler
import com.team.prezel.feature.analysis.impl.result.AnalysisFailedScreen
import com.team.prezel.feature.analysis.impl.result.AnalysisLoadingScreen
import com.team.prezel.feature.analysis.impl.result.FileRecognitionFailedScreen
import com.team.prezel.feature.analysis.impl.result.ScriptFileRecognitionFailedScreen
import com.team.prezel.feature.analysis.impl.schedule.PresentationScheduleScreen
import com.team.prezel.feature.analysis.impl.script.ScriptInputScreen
import com.team.prezel.feature.analysis.impl.situation.PresentationSituationScreen
import kotlinx.coroutines.launch

private const val RECORDING_VOLUME_SAMPLE_INTERVAL_MILLIS = 50
private const val VOICE_RECORDING_FEEDBACK_DURATION_MILLIS = 3_000
private const val VOICE_RECORDING_FEEDBACK_SAMPLE_COUNT =
    VOICE_RECORDING_FEEDBACK_DURATION_MILLIS / RECORDING_VOLUME_SAMPLE_INTERVAL_MILLIS
private const val SILENCE_VOLUME_THRESHOLD = 0.12f
private const val LOW_AVERAGE_VOLUME_THRESHOLD = 0.25f

@Composable
internal fun AnalysisScreen(
    onBack: () -> Unit,
    navigateToStep: (step: AnalysisFlowStep, clearStack: Boolean) -> Unit,
    navigateToReport: (presentationId: Long) -> Unit,
    viewModel: AnalysisFlowViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = LocalSnackbarCoroutineScope.current
    var isScriptExpanded by rememberSaveable(uiState.step) { mutableStateOf(false) }

    BackHandler {
        viewModel.onIntent(AnalysisFlowUiIntent.Back)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AnalysisFlowUiEffect.NavigateBack -> onBack()
                is AnalysisFlowUiEffect.NavigateToStep -> navigateToStep(effect.step, effect.clearStack)
                is AnalysisFlowUiEffect.NavigateToReport -> {
                    navigateToReport(effect.presentationId)
                    scope.launch {
                        snackbarHostState.showPrezelSnackbar(
                            message = resources.getString(R.string.feature_analysis_impl_analyze_complete_message),
                            useRaisedPosition = false,
                        )
                    }
                }
                is AnalysisFlowUiEffect.ShowMessage -> {
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(effect.message.toStringRes()))
                }
            }
        }
    }

    AnalysisScreen(
        uiState = uiState,
        isScriptExpanded = isScriptExpanded,
        onIntent = viewModel::onIntent,
        onScriptExpandedChange = { isScriptExpanded = it },
    )
}

@StringRes
private fun AnalysisUiMessage.toStringRes(): Int =
    when (this) {
        AnalysisUiMessage.AUTH_EXPIRED -> R.string.feature_analysis_impl_error_auth_expired
        AnalysisUiMessage.ANALYSIS_FAILED -> R.string.feature_analysis_impl_error_analysis_failed
        AnalysisUiMessage.SCRIPT_LOAD_FAILED -> R.string.feature_analysis_impl_error_script_load_failed
        AnalysisUiMessage.SCRIPT_FILE_LOAD_FAILED -> R.string.feature_analysis_impl_error_script_file_load_failed
        AnalysisUiMessage.NETWORK_FAILED -> R.string.feature_analysis_impl_error_network_failed
        AnalysisUiMessage.UNKNOWN_FAILED -> R.string.feature_analysis_impl_error_unknown_failed
        AnalysisUiMessage.RECORD_AUDIO_PERMISSION_DENIED -> R.string.feature_analysis_impl_voice_recording_permission_denied
        AnalysisUiMessage.RECORD_AUDIO_PERMISSION_PERMANENTLY_DENIED ->
            R.string.feature_analysis_impl_voice_recording_permission_permanently_denied

        AnalysisUiMessage.RECORDING_START_FAILED -> R.string.feature_analysis_impl_voice_recording_failed
        AnalysisUiMessage.RECORDING_STOP_FAILED -> R.string.feature_analysis_impl_voice_recording_stop_failed
        AnalysisUiMessage.PLAYBACK_START_FAILED -> R.string.feature_analysis_impl_voice_recording_playback_failed
    }

private fun AnalysisFlowUiState.statusBarStyle(isScriptExpanded: Boolean): EdgeToEdgeStatusBarStyle =
    when (step) {
        AnalysisFlowStep.VOICE_RECORDING if isScriptExpanded -> EdgeToEdgeStatusBarStyle.BG_REGULAR
        AnalysisFlowStep.VOICE_RECORDING if recordingState.isCompleted -> EdgeToEdgeStatusBarStyle.BG_REGULAR
        AnalysisFlowStep.VOICE_RECORDING -> EdgeToEdgeStatusBarStyle.BG_MEDIUM
        else -> EdgeToEdgeStatusBarStyle.DEFAULT
    }

@Composable
private fun AnalysisScreen(
    uiState: AnalysisFlowUiState,
    isScriptExpanded: Boolean,
    onIntent: (AnalysisFlowUiIntent) -> Unit,
    onScriptExpandedChange: (Boolean) -> Unit,
) {
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val voiceRecordingFeedback = uiState.voiceRecordingFeedback
    val voiceRecordingChromeUi = voiceRecordingFeedback?.toChromeUi()

    LaunchedEffect(uiState.step) {
        if (uiState.step == AnalysisFlowStep.VOICE_RECORDING) {
            snackbarHostState.showPrezelSnackbar(
                message = resources.getString(R.string.feature_analysis_impl_voice_recording_guide),
            )
        }
    }

    LaunchedEffect(voiceRecordingFeedback) {
        if (voiceRecordingFeedback != null) {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    val onClickRecordingControl = rememberVoiceRecordingControlClick(
        uiState = uiState,
        onIntent = onIntent,
    )

    AnalysisStepContent(
        uiState = uiState,
        isScriptExpanded = isScriptExpanded,
        voiceRecordingChromeUi = voiceRecordingChromeUi,
        onIntent = onIntent,
        onClickRecordingControl = onClickRecordingControl,
        onScriptExpandedChange = onScriptExpandedChange,
    )
}

private enum class VoiceRecordingFeedback {
    READY_TO_CONTINUE,
    SPEAK_LOUDER,
}

private fun VoiceRecordingFeedback.toChromeUi(): VoiceRecordingChromeUi =
    when (this) {
        VoiceRecordingFeedback.READY_TO_CONTINUE -> VoiceRecordingChromeUi(
            titleResId = R.string.feature_analysis_impl_voice_recording_ready_to_continue_feedback,
            status = VoiceChromeStatus.LISTENING,
            gradient = VoiceChromeGradient.NONE,
            hideWaveform = true,
        )

        VoiceRecordingFeedback.SPEAK_LOUDER -> VoiceRecordingChromeUi(
            titleResId = R.string.feature_analysis_impl_voice_recording_speak_louder_feedback,
        )
    }

private val AnalysisFlowUiState.voiceRecordingFeedback: VoiceRecordingFeedback?
    get() {
        if (step != AnalysisFlowStep.VOICE_RECORDING || recordingState !is AudioSessionState.Recording) return null

        val recentVolumes = recordingVolumes.takeLast(VOICE_RECORDING_FEEDBACK_SAMPLE_COUNT)
        if (recentVolumes.size < VOICE_RECORDING_FEEDBACK_SAMPLE_COUNT) return null

        if (recentVolumes.all { volume -> volume <= SILENCE_VOLUME_THRESHOLD }) {
            return VoiceRecordingFeedback.READY_TO_CONTINUE
        }

        val averageVolume = recentVolumes.average().toFloat()
        return if (averageVolume <= LOW_AVERAGE_VOLUME_THRESHOLD) {
            VoiceRecordingFeedback.SPEAK_LOUDER
        } else {
            null
        }
    }

@Composable
private fun rememberVoiceRecordingControlClick(
    uiState: AnalysisFlowUiState,
    onIntent: (AnalysisFlowUiIntent) -> Unit,
): () -> Unit {
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()

    return rememberAnalysisRecordAudioPermissionControlClickHandler(
        recordingState = uiState.recordingState,
        onClickRecordingControl = { onIntent(AnalysisFlowUiIntent.ClickRecordingControl) },
        onPermissionDenied = {
            coroutineScope.launch {
                snackbarHostState.showPrezelSnackbar(
                    message = resources.getString(R.string.feature_analysis_impl_voice_recording_permission_denied),
                )
            }
        },
        onPermissionPermanentlyDenied = {
            coroutineScope.launch {
                snackbarHostState.showPrezelSnackbar(
                    message = resources.getString(R.string.feature_analysis_impl_voice_recording_permission_permanently_denied),
                )
            }
        },
    )
}

@Composable
private fun AnalysisStepContent(
    uiState: AnalysisFlowUiState,
    isScriptExpanded: Boolean,
    voiceRecordingChromeUi: VoiceRecordingChromeUi?,
    onIntent: (AnalysisFlowUiIntent) -> Unit,
    onClickRecordingControl: () -> Unit,
    onScriptExpandedChange: (Boolean) -> Unit,
) {
    when (uiState.step) {
        AnalysisFlowStep.PRESENTATION_SCHEDULE,
        AnalysisFlowStep.PRESENTATION_SITUATION,
        AnalysisFlowStep.SCRIPT_INPUT,
        AnalysisFlowStep.AUDIO_UPLOAD,
        AnalysisFlowStep.VOICE_RECORDING,
        -> AnalysisInputStepContent(
            uiState = uiState,
            isScriptExpanded = isScriptExpanded,
            voiceRecordingChromeUi = voiceRecordingChromeUi,
            onIntent = onIntent,
            onClickRecordingControl = onClickRecordingControl,
            onScriptExpandedChange = onScriptExpandedChange,
        )

        AnalysisFlowStep.ANALYZING,
        AnalysisFlowStep.ANALYSIS_FAILED,
        AnalysisFlowStep.FILE_RECOGNITION_FAILED,
        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
        -> AnalysisResultStepContent(
            step = uiState.step,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun AnalysisInputStepContent(
    uiState: AnalysisFlowUiState,
    isScriptExpanded: Boolean,
    voiceRecordingChromeUi: VoiceRecordingChromeUi?,
    onIntent: (AnalysisFlowUiIntent) -> Unit,
    onClickRecordingControl: () -> Unit,
    onScriptExpandedChange: (Boolean) -> Unit,
) {
    if (uiState.step == AnalysisFlowStep.VOICE_RECORDING) {
        VoiceRecordingStatusBarStyle(style = uiState.statusBarStyle(isScriptExpanded = isScriptExpanded))

        VoiceRecordingScreen(
            uiState = uiState,
            isScriptExpanded = isScriptExpanded,
            voiceChromeUi = voiceRecordingChromeUi,
            onClickRecordingControl = onClickRecordingControl,
            onStopRecording = { onIntent(AnalysisFlowUiIntent.StopRecording) },
            onResetRecording = { onIntent(AnalysisFlowUiIntent.ResetRecording) },
            onAnalyze = { onIntent(AnalysisFlowUiIntent.Next) },
            onScriptExpandedChange = onScriptExpandedChange,
            onBack = { onIntent(AnalysisFlowUiIntent.Back) },
        )
        return
    }

    AnalysisFormInputStepContent(
        uiState = uiState,
        onIntent = onIntent,
    )
}

@Composable
private fun AnalysisFormInputStepContent(
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
            onAudioFileSelected = { fileUri, fileName ->
                onIntent(
                    AnalysisFlowUiIntent.SelectAudioFile(
                        fileUri = fileUri,
                        fileName = fileName,
                    ),
                )
            },
            onAnalyze = { onIntent(AnalysisFlowUiIntent.Next) },
            onBack = { onIntent(AnalysisFlowUiIntent.Back) },
        )

        AnalysisFlowStep.VOICE_RECORDING,
        AnalysisFlowStep.ANALYZING,
        AnalysisFlowStep.ANALYSIS_FAILED,
        AnalysisFlowStep.FILE_RECOGNITION_FAILED,
        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
        -> Unit
    }
}

@Composable
private fun AnalysisResultStepContent(
    step: AnalysisFlowStep,
    onIntent: (AnalysisFlowUiIntent) -> Unit,
) {
    when (step) {
        AnalysisFlowStep.ANALYZING -> AnalysisLoadingScreen()

        AnalysisFlowStep.ANALYSIS_FAILED -> AnalysisFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.AUDIO)) },
        )

        AnalysisFlowStep.FILE_RECOGNITION_FAILED -> FileRecognitionFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.AUDIO)) },
        )

        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED -> ScriptFileRecognitionFailedScreen(
            onRetry = { onIntent(AnalysisFlowUiIntent.RetryFileUpload(AnalysisUploadType.SCRIPT)) },
        )

        AnalysisFlowStep.PRESENTATION_SCHEDULE,
        AnalysisFlowStep.PRESENTATION_SITUATION,
        AnalysisFlowStep.SCRIPT_INPUT,
        AnalysisFlowStep.AUDIO_UPLOAD,
        AnalysisFlowStep.VOICE_RECORDING,
        -> Unit
    }
}
