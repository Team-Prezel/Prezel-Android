package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val SUPPORTED_AUDIO_FILE_EXTENSIONS = setOf("m4a", "mp4", "mp3")

@HiltViewModel(assistedFactory = AnalysisFlowViewModel.Factory::class)
internal class AnalysisFlowViewModel @AssistedInject constructor(
    @Assisted isFromReport: Boolean,
    private val analyzePresentationUseCase: AnalyzePresentationUseCase,
    private val reAnalyzePresentationUseCase: ReAnalyzePresentationUseCase,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
    private val fetchPresentationScriptDetailUseCase: FetchPresentationScriptDetailUseCase,
    private val analysisFileCache: AnalysisFileCache,
    private val audioController: RecordingAudioController,
) : BaseViewModel<AnalysisFlowUiState, AnalysisFlowUiIntent, AnalysisFlowUiEffect>(AnalysisFlowUiState(isFromReport = isFromReport)) {
    @AssistedFactory
    interface Factory {
        fun create(isFromReport: Boolean): AnalysisFlowViewModel
    }

    private var analyzeJob: Job? = null

    init {
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
            audioController.audioSessionEffect.collect {
                updateState { copy(step = AnalysisFlowStep.ANALYSIS_FAILED) }
            }
        }
    }

    override fun onIntent(intent: AnalysisFlowUiIntent) {
        intent.reduceFormOrNull(currentState.form)?.let { nextForm ->
            updateState { copy(form = nextForm) }
            return
        }
        if (intent.handleRecordingIntent(audioController, currentState.recordingState)) return

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

            is AnalysisFlowUiIntent.SelectScriptFile -> selectScriptFile(intent.fileUri)
            is AnalysisFlowUiIntent.SelectAudioFile -> selectAudioFile(
                fileUri = intent.fileUri,
                fileName = intent.fileName,
            )

            is AnalysisFlowUiIntent.RetryFileUpload -> retryFileUpload(intent.uploadType)
            AnalysisFlowUiIntent.Next -> moveNext()
            AnalysisFlowUiIntent.SkipScript -> skipScript()
            AnalysisFlowUiIntent.Back -> moveBack()
            else -> Unit
        }
    }

    private fun selectAudioFile(
        fileUri: String?,
        fileName: String?,
    ) {
        if (!fileName.isSupportedAudioFileName()) {
            updateState {
                copy(
                    step = AnalysisFlowStep.FILE_RECOGNITION_FAILED,
                    form = form.copy(audioFileUri = null),
                )
            }
            return
        }

        updateState {
            copy(form = form.copy(audioFileUri = fileUri))
        }
    }

    private fun selectScriptFile(fileUri: String?) {
        updateState {
            copy(
                form = form.copy(
                    scriptFileUri = fileUri,
                    script = if (fileUri == null) "" else form.script,
                ),
            )
        }

        if (fileUri == null) return

        viewModelScope.launch {
            runCatching { withContext(Dispatchers.IO) { analysisFileCache.readTextFromUri(fileUri) } }
                .onSuccess { script ->
                    if (currentState.form.scriptFileUri == fileUri) {
                        updateState { copy(form = form.copy(script = script)) }
                    }
                }.onFailure {
                    if (currentState.form.scriptFileUri == fileUri) {
                        updateState { copy(form = form.copy(script = "")) }
                    }
                    sendEffect(AnalysisFlowUiEffect.ShowMessage(AnalysisUiMessage.SCRIPT_FILE_LOAD_FAILED))
                }
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
            AnalysisFlowStep.ANALYSIS_FAILED,
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
        audioController.stopPlayback()

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

            val analysisResult = submission.analyzePresentationRecording(
                analysisFileCache = analysisFileCache,
                analyzePresentationUseCase = analyzePresentationUseCase,
            )

            analysisResult.fold(
                onSuccess = { result ->
                    if (currentState.step == AnalysisFlowStep.ANALYZING) {
                        audioController.reset()
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

            val reAnalyzeResult = submission.reAnalyzePresentationRecording(
                presentationId = presentationId,
                analysisFileCache = analysisFileCache,
                reAnalyzePresentationUseCase = reAnalyzePresentationUseCase,
            )

            reAnalyzeResult.fold(
                onSuccess = { result ->
                    if (currentState.step == AnalysisFlowStep.ANALYZING) {
                        audioController.reset()
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
                .onSuccess { detail ->
                    detail.analysisSummary
                        .fetchOriginalScript(fetchPresentationScriptDetailUseCase)
                        .onSuccess { script ->
                            updateState {
                                copy(
                                    form = detail.analysisSummary.toAnalysisForm().copy(
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
                .onSuccess { detail ->
                    updateState {
                        copy(
                            form = detail.analysisSummary.toAnalysisForm().copy(scriptInputType = ScriptInputType.DIRECT_INPUT),
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

    private fun handleAnalysisFailure(action: AnalysisFailureAction) {
        when (action) {
            AnalysisFailureAction.NavigateHome -> {
                audioController.reset()
                viewModelScope.launch {
                    sendEffect(AnalysisFlowUiEffect.NavigateHome)
                }
            }

            AnalysisFailureAction.RetryAnalysis -> {
                updateState { copy(step = AnalysisFlowStep.ANALYSIS_FAILED) }
            }

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
                val retryStep = currentState.audioRetryStep
                updateState {
                    copy(
                        step = retryStep,
                        form = form.copy(audioFileUri = null),
                    )
                }
                if (retryStep == AnalysisFlowStep.VOICE_RECORDING) {
                    audioController.reset()
                }
                viewModelScope.launch {
                    sendEffect(
                        AnalysisFlowUiEffect.NavigateToStep(
                            step = retryStep,
                            clearStack = true,
                        ),
                    )
                }
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

        if (currentState.shouldResetAudioOnBack) {
            audioController.reset()
        }

        currentState.backClearedFormOrNull()?.let { clearedForm ->
            updateState { copy(form = clearedForm) }
        }

        viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateBack) }
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

private val AnalysisFlowUiState.audioRetryStep: AnalysisFlowStep
    get() = when (step) {
        AnalysisFlowStep.ANALYSIS_FAILED -> AnalysisFlowStep.VOICE_RECORDING
        AnalysisFlowStep.FILE_RECOGNITION_FAILED -> when (startType) {
            AnalysisStartType.FILE_UPLOAD -> AnalysisFlowStep.SCRIPT_INPUT
            AnalysisStartType.VOICE_RECORDING -> AnalysisFlowStep.VOICE_RECORDING
        }

        else -> audioInputStep
    }

private suspend fun PresentationAnalysisSummary.fetchOriginalScript(
    fetchPresentationScriptDetailUseCase: FetchPresentationScriptDetailUseCase,
): Result<String?> {
    if (accuracyScore == null || scriptMatchRate == null) return Result.success(null)

    return fetchPresentationScriptDetailUseCase(analysisResultId = analysisResultId)
        .map { it.originalScript }
}

private suspend fun PresentationAnalysisSubmission.analyzePresentationRecording(
    analysisFileCache: AnalysisFileCache,
    analyzePresentationUseCase: AnalyzePresentationUseCase,
): Result<Long> {
    val audioFilePath = resolveAudioFilePath(analysisFileCache)
    val scriptFilePath = resolveScriptFilePath(analysisFileCache)
    return analyzePresentationUseCase(
        name = name,
        date = date.toRequestDate(),
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
        script = script,
        scriptFilePath = scriptFilePath,
        audioFilePath = audioFilePath,
    )
}

private suspend fun PresentationAnalysisSubmission.reAnalyzePresentationRecording(
    presentationId: Long,
    analysisFileCache: AnalysisFileCache,
    reAnalyzePresentationUseCase: ReAnalyzePresentationUseCase,
): Result<PresentationAnalysisSummary> {
    val audioFilePath = resolveAudioFilePath(analysisFileCache)
    val scriptFilePath = resolveScriptFilePath(analysisFileCache)
    return reAnalyzePresentationUseCase(
        presentationId = presentationId,
        script = script,
        scriptFilePath = scriptFilePath,
        audioFilePath = audioFilePath,
    )
}

private fun PresentationAnalysisSubmission.resolveAudioFilePath(analysisFileCache: AnalysisFileCache): String =
    audioFileUri
        ?.let { uri ->
            analysisFileCache
                .copyUriToCache(
                    uriString = uri,
                    prefix = "audio",
                ).absolutePath
        }
        ?: recordingFilePath

private fun PresentationAnalysisSubmission.resolveScriptFilePath(analysisFileCache: AnalysisFileCache): String? =
    scriptFileUri?.let { uri ->
        analysisFileCache
            .copyUriToCache(
                uriString = uri,
                prefix = "script",
            ).absolutePath
    }

private fun String?.isSupportedAudioFileName(): Boolean {
    if (this == null) return true

    val extension = substringAfterLast('.', missingDelimiterValue = "").lowercase()

    return extension in SUPPORTED_AUDIO_FILE_EXTENSIONS
}

private fun AnalysisFlowUiIntent.handleRecordingIntent(
    audioController: RecordingAudioController,
    recordingState: AudioSessionState,
): Boolean {
    when (this) {
        AnalysisFlowUiIntent.ClickRecordingControl -> audioController.handleControlClick(recordingState)
        AnalysisFlowUiIntent.StopRecording -> audioController.stopRecording()
        AnalysisFlowUiIntent.ResetRecording -> audioController.reset()
        else -> return false
    }
    return true
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
