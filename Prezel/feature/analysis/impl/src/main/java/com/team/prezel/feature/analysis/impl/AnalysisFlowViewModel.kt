package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.presentation.AnalyzePresentationUseCase
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationDetailUseCase
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationScriptDetailUseCase
import com.team.prezel.core.domain.usecase.presentation.ReAnalyzePresentationUseCase
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.analysis.api.AnalysisStartType
import com.team.prezel.feature.analysis.impl.cache.AnalysisFileCache
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiEffect
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.contract.ScriptInputType
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AnalysisFlowViewModel @Inject constructor(
    private val analyzePresentationUseCase: AnalyzePresentationUseCase,
    private val reAnalyzePresentationUseCase: ReAnalyzePresentationUseCase,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
    private val fetchPresentationScriptDetailUseCase: FetchPresentationScriptDetailUseCase,
    private val analysisFileCache: AnalysisFileCache,
    private val audioController: RecordingAudioController,
) : BaseViewModel<AnalysisFlowUiState, AnalysisFlowUiIntent, AnalysisFlowUiEffect>(AnalysisFlowUiState()) {
    private var analyzeJob: Job? = null

    init {
        collectAudioSession()
    }

    override fun onIntent(intent: AnalysisFlowUiIntent) {
        intent.reduceFormOrNull(currentState.form)?.let { nextForm ->
            updateState { copy(form = nextForm) }
            return
        }

        when (intent) {
            is AnalysisFlowUiIntent.EnterStep -> updateState {
                copy(
                    step = intent.step,
                    startType = intent.startType,
                )
            }
            is AnalysisFlowUiIntent.StartReRecording -> startReRecording(
                presentationId = intent.presentationId,
                isPast = intent.isPast,
            )

            is AnalysisFlowUiIntent.StartReWritingScript -> startReWritingScript(
                presentationId = intent.presentationId,
                isPast = intent.isPast,
            )

            AnalysisFlowUiIntent.ClickRecordingControl -> audioController.handleControlClick(currentState.recordingState)
            AnalysisFlowUiIntent.StopRecording -> audioController.stopRecording()
            AnalysisFlowUiIntent.ResetRecording -> audioController.reset()
            is AnalysisFlowUiIntent.RetryFileUpload -> retryFileUpload(intent.uploadType)
            AnalysisFlowUiIntent.Next -> moveNext()
            AnalysisFlowUiIntent.SkipScript -> skipScript()
            AnalysisFlowUiIntent.Back -> moveBack()
            else -> Unit
        }
    }

    private fun moveNext() {
        if (!currentState.canMoveNext) return

        if (currentState.step == AnalysisFlowStep.VOICE_RECORDING || currentState.step == AnalysisFlowStep.AUDIO_UPLOAD) {
            analyzePresentation()
            return
        }

        val nextStep = when (currentState.step) {
            AnalysisFlowStep.PRESENTATION_SCHEDULE -> AnalysisFlowStep.PRESENTATION_SITUATION
            AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisFlowStep.SCRIPT_INPUT
            AnalysisFlowStep.SCRIPT_INPUT -> currentState.audioInputStep
            AnalysisFlowStep.AUDIO_UPLOAD,
            AnalysisFlowStep.VOICE_RECORDING,
            AnalysisFlowStep.ANALYZING,
            AnalysisFlowStep.FILE_RECOGNITION_FAILED,
            AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
            -> currentState.step
        }

        updateState { copy(step = nextStep) }
        viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = nextStep)) }
    }

    private fun analyzePresentation() {
        val submission = currentState.toPresentationAnalysisSubmissionOrNull() ?: return
        val reAnalyzePresentationId = currentState.reRecordingPresentationId
            ?: currentState.reWritingScriptPresentationId

        if (reAnalyzePresentationId != null) {
            reAnalyzePresentation(
                presentationId = reAnalyzePresentationId,
                submission = submission,
            )
            return
        }

        updateState { copy(step = AnalysisFlowStep.ANALYZING) }

        analyzeJob?.cancel()
        analyzeJob = viewModelScope.launch {
            sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = AnalysisFlowStep.ANALYZING))

            val analysisResult = submission.analyzePresentationRecording()

            analysisResult.fold(
                onSuccess = { result ->
                    if (currentState.step == AnalysisFlowStep.ANALYZING) {
                        audioController.release()
                        sendEffect(AnalysisFlowUiEffect.NavigateToReport(presentationId = result))
                    }
                },
                onFailure = { throwable ->
                    handleAnalysisFailure(throwable.toAnalysisFailureAction())
                },
            )
        }
    }

    private fun reAnalyzePresentation(
        presentationId: Long,
        submission: PresentationAnalysisSubmission,
    ) {
        updateState { copy(step = AnalysisFlowStep.ANALYZING) }

        analyzeJob?.cancel()
        analyzeJob = viewModelScope.launch {
            sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = AnalysisFlowStep.ANALYZING))

            val reAnalyzeResult = submission.reAnalyzePresentationRecording(presentationId)

            reAnalyzeResult.fold(
                onSuccess = { result ->
                    if (currentState.step == AnalysisFlowStep.ANALYZING) {
                        audioController.release()
                        sendEffect(AnalysisFlowUiEffect.NavigateToReport(presentationId = result.presentationId))
                    }
                },
                onFailure = { throwable ->
                    handleAnalysisFailure(throwable.toAnalysisFailureAction())
                },
            )
        }
    }

    private fun startReRecording(
        presentationId: Long,
        isPast: Boolean,
    ) {
        if (currentState.reRecordingPresentationId == presentationId) return

        audioController.reset()
        updateState {
            copy(
                step = AnalysisFlowStep.VOICE_RECORDING,
                reRecordingPresentationId = presentationId,
            )
        }

        viewModelScope.launch {
            fetchPresentationDetailUseCase(presentationId = presentationId, isPast = isPast)
                .onSuccess { summary ->
                    summary
                        .fetchOriginalScript(fetchPresentationScriptDetailUseCase)
                        .onSuccess { script ->
                            updateState {
                                copy(
                                    form = summary.toAnalysisForm().copy(
                                        scriptInputType = ScriptInputType.DIRECT_INPUT,
                                        script = script.orEmpty(),
                                    ),
                                    step = AnalysisFlowStep.VOICE_RECORDING,
                                    reRecordingPresentationId = presentationId,
                                )
                            }
                        }.onFailure {
                            sendEffect(AnalysisFlowUiEffect.ShowMessage(AnalysisUiMessage.SCRIPT_LOAD_FAILED))
                            sendEffect(AnalysisFlowUiEffect.NavigateBack)
                        }
                }.onFailure {
                    sendEffect(AnalysisFlowUiEffect.ShowMessage(AnalysisUiMessage.ANALYSIS_FAILED))
                    sendEffect(AnalysisFlowUiEffect.NavigateBack)
                }
        }
    }

    private fun startReWritingScript(
        presentationId: Long,
        isPast: Boolean,
    ) {
        if (currentState.reWritingScriptPresentationId == presentationId && currentState.step == AnalysisFlowStep.SCRIPT_INPUT) return

        audioController.reset()
        updateState {
            copy(
                step = AnalysisFlowStep.SCRIPT_INPUT,
                reWritingScriptPresentationId = presentationId,
            )
        }

        viewModelScope.launch {
            fetchPresentationDetailUseCase(presentationId = presentationId, isPast = isPast)
                .onSuccess { summary ->
                    updateState {
                        copy(
                            form = summary.toAnalysisForm().copy(scriptInputType = ScriptInputType.DIRECT_INPUT),
                            step = AnalysisFlowStep.SCRIPT_INPUT,
                            reWritingScriptPresentationId = presentationId,
                        )
                    }
                }.onFailure {
                    sendEffect(AnalysisFlowUiEffect.ShowMessage(AnalysisUiMessage.ANALYSIS_FAILED))
                    sendEffect(AnalysisFlowUiEffect.NavigateBack)
                }
        }
    }

    private suspend fun PresentationAnalysisSubmission.analyzePresentationRecording(): Result<Long> =
        runCatching {
            val audioFilePath = audioFileUri
                ?.let { uri ->
                    val audioFile = analysisFileCache.copyUriToCache(
                        uriString = uri,
                        prefix = "audio",
                    )
                    audioFile.absolutePath
                }
                ?: recordingFilePath
            val scriptFile = scriptFileUri?.let { uri ->
                analysisFileCache.copyUriToCache(
                    uriString = uri,
                    prefix = "script",
                )
            }
            analyzePresentationUseCase(
                name = name,
                date = date.toRequestDate(),
                category = category,
                purpose = purpose,
                style = style,
                audience = audience,
                script = script,
                scriptFilePath = scriptFile?.absolutePath,
                audioFilePath = audioFilePath,
            ).getOrThrow()
        }

    private suspend fun PresentationAnalysisSubmission.reAnalyzePresentationRecording(presentationId: Long): Result<PresentationAnalysisSummary> =
        runCatching {
            val audioFilePath = audioFileUri
                ?.let { uri ->
                    val audioFile = analysisFileCache.copyUriToCache(
                        uriString = uri,
                        prefix = "audio",
                    )
                    audioFile.absolutePath
                }
                ?: recordingFilePath
            val scriptFile = scriptFileUri?.let { uri ->
                analysisFileCache.copyUriToCache(
                    uriString = uri,
                    prefix = "script",
                )
            }
            reAnalyzePresentationUseCase(
                presentationId = presentationId,
                script = script,
                scriptFilePath = scriptFile?.absolutePath,
                audioFilePath = audioFilePath,
            ).getOrThrow()
        }

    private fun handleAnalysisFailure(action: AnalysisFailureAction) {
        when (action) {
            is AnalysisFailureAction.RetryFileUpload -> {
                val failureStep = when (action.uploadType) {
                    AnalysisUploadType.AUDIO -> AnalysisFlowStep.FILE_RECOGNITION_FAILED
                    AnalysisUploadType.SCRIPT -> AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED
                }

                updateState {
                    copy(
                        step = failureStep,
                    )
                }
            }

            is AnalysisFailureAction.ShowMessage -> {
                val retryStep = currentState.audioInputStep
                viewModelScope.launch {
                    sendEffect(AnalysisFlowUiEffect.ShowMessage(action.message))
                }
                updateState { copy(step = retryStep) }
            }
        }
    }

    private fun retryFileUpload(uploadType: AnalysisUploadType) {
        when (uploadType) {
            AnalysisUploadType.SCRIPT -> {
                val retryStep = AnalysisFlowStep.SCRIPT_INPUT
                updateState {
                    copy(
                        step = retryStep,
                        form = form.copy(
                            scriptInputType = ScriptInputType.FILE_UPLOAD,
                            scriptFileUri = null,
                        ),
                    )
                }
                viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = retryStep)) }
            }

            AnalysisUploadType.AUDIO -> {
                val retryStep = currentState.audioInputStep
                updateState {
                    copy(
                        step = retryStep,
                        form = form.copy(audioFileUri = null),
                    )
                }
                if (retryStep == AnalysisFlowStep.VOICE_RECORDING) {
                    audioController.reset()
                }
                viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = retryStep)) }
            }
        }
    }

    private fun skipScript() {
        if (currentState.step != AnalysisFlowStep.SCRIPT_INPUT) return

        val nextStep = currentState.audioInputStep
        updateState {
            copy(
                step = nextStep,
                form = form.copy(
                    script = "",
                    scriptFileUri = null,
                ),
            )
        }
        viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = nextStep)) }
    }

    private fun moveBack() {
        if (currentState.step == AnalysisFlowStep.ANALYZING) analyzeJob?.cancel()

        if (currentState.shouldResetRecordingOnBack) {
            audioController.reset()
        }

        currentState.backClearedFormOrNull()?.let { clearedForm ->
            updateState {
                copy(form = clearedForm)
            }
        }

        if (currentState.shouldReleaseAudioOnBack) {
            audioController.release()
        }

        viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateBack) }
    }

    private fun collectAudioSession() {
        viewModelScope.launch {
            audioController.audioSessionState.collect { audioState ->
                updateState { copy(recordingState = audioState) }
            }
        }

        viewModelScope.launch {
            audioController.recordingVolumes.collect { volumes ->
                updateState { copy(recordingVolumes = volumes) }
            }
        }

        viewModelScope.launch {
            audioController.audioSessionEffect.collect { effect ->
                sendEffect(AnalysisFlowUiEffect.ShowMessage(effect.toUiMessage()))
            }
        }
    }

    override fun onCleared() {
        audioController.release()
        super.onCleared()
    }
}

private val AnalysisFlowUiState.audioInputStep: AnalysisFlowStep
    get() = when (startType) {
        AnalysisStartType.FILE_UPLOAD -> AnalysisFlowStep.AUDIO_UPLOAD
        AnalysisStartType.VOICE_RECORDING -> AnalysisFlowStep.VOICE_RECORDING
    }

private suspend fun PresentationAnalysisSummary.fetchOriginalScript(
    fetchPresentationScriptDetailUseCase: FetchPresentationScriptDetailUseCase,
): Result<String?> {
    if (accuracyScore == null || scriptMatchRate == null) return Result.success(null)

    return fetchPresentationScriptDetailUseCase(analysisResultId = analysisResultId)
        .map { it.originalScript }
}

private fun AudioSessionEffect.toUiMessage(): AnalysisUiMessage =
    when (this) {
        AudioSessionEffect.RecordingStartFailed -> AnalysisUiMessage.RECORDING_START_FAILED
        AudioSessionEffect.RecordingStopFailed -> AnalysisUiMessage.RECORDING_STOP_FAILED
        AudioSessionEffect.PlaybackStartFailed -> AnalysisUiMessage.PLAYBACK_START_FAILED
    }

private fun RecordingAudioController.handleControlClick(recordingState: AudioSessionState) {
    when (recordingState) {
        AudioSessionState.Idle -> startRecording()
        is AudioSessionState.Recording -> pauseRecording()
        is AudioSessionState.PausedRecording -> resumeRecording()
        is AudioSessionState.ReadyToPlay -> startPlayback()
        is AudioSessionState.Playing -> stopPlayback()
    }
}
