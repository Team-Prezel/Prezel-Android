package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.presentation.AnalyzePresentationUseCase
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationDetailUseCase
import com.team.prezel.core.domain.usecase.presentation.ReAnalyzePresentationUseCase
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.analysis.impl.cache.AnalysisFileCache
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiEffect
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import com.team.prezel.feature.analysis.impl.contract.AnalysisSituationOption
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.contract.ScriptInputType
import com.team.prezel.feature.analysis.impl.contract.recordingFilePath
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class AnalysisFlowViewModel @Inject constructor(
    private val analyzePresentationUseCase: AnalyzePresentationUseCase,
    private val reAnalyzePresentationUseCase: ReAnalyzePresentationUseCase,
    private val fetchPresentationDetailUseCase: FetchPresentationDetailUseCase,
    private val analysisFileCache: AnalysisFileCache,
    private val audioController: RecordingAudioController,
) : BaseViewModel<AnalysisFlowUiState, AnalysisFlowUiIntent, AnalysisFlowUiEffect>(AnalysisFlowUiState()) {
    private var analyzeJob: Job? = null

    init {
        collectAudioSessionState()
        collectRecordingVolumes()
        collectAudioSessionEffect()
    }

    override fun onIntent(intent: AnalysisFlowUiIntent) {
        intent.reduceFormOrNull(currentState.form)?.let { nextForm ->
            updateState { copy(form = nextForm) }
            return
        }

        when (intent) {
            is AnalysisFlowUiIntent.EnterStep -> updateState { copy(step = intent.step) }
            is AnalysisFlowUiIntent.StartReRecording -> startReRecording(
                presentationId = intent.presentationId,
                isPast = intent.isPast,
            )

            is AnalysisFlowUiIntent.StartReWritingScript -> startReWritingScript(
                presentationId = intent.presentationId,
                isPast = intent.isPast,
            )

            AnalysisFlowUiIntent.ClickRecordingControl -> handleRecordingControlClick()
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
            AnalysisFlowStep.SCRIPT_INPUT -> AnalysisFlowStep.VOICE_RECORDING
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
                    updateState {
                        copy(
                            form = summary.toAnalysisForm(),
                            step = AnalysisFlowStep.VOICE_RECORDING,
                            reRecordingPresentationId = presentationId,
                        )
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

    private suspend fun PresentationAnalysisSubmission.reAnalyzePresentationRecording(
        presentationId: Long,
    ): Result<PresentationAnalysisSummary> =
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
                val retryStep = AnalysisFlowStep.VOICE_RECORDING
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
                val retryStep = AnalysisFlowStep.VOICE_RECORDING
                updateState {
                    copy(
                        step = retryStep,
                        form = form.copy(audioFileUri = null),
                    )
                }
                audioController.reset()
                viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateToStep(step = retryStep)) }
            }
        }
    }

    private fun skipScript() {
        if (currentState.step != AnalysisFlowStep.SCRIPT_INPUT) return

        val nextStep = AnalysisFlowStep.VOICE_RECORDING
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

        if (
            currentState.step == AnalysisFlowStep.PRESENTATION_SCHEDULE ||
            currentState.reRecordingPresentationId != null ||
            currentState.reWritingScriptPresentationId != null
        ) {
            audioController.release()
        }

        viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateBack) }
    }

    private fun handleRecordingControlClick() {
        when (currentState.recordingState) {
            AudioSessionState.Idle -> audioController.startRecording()
            is AudioSessionState.Recording -> audioController.pauseRecording()
            is AudioSessionState.PausedRecording -> audioController.resumeRecording()
            is AudioSessionState.ReadyToPlay -> audioController.startPlayback()
            is AudioSessionState.Playing -> audioController.stopPlayback()
        }
    }

    private fun collectAudioSessionState() {
        viewModelScope.launch {
            audioController.audioSessionState.collect { audioState ->
                updateState { copy(recordingState = audioState) }
            }
        }
    }

    private fun collectRecordingVolumes() {
        viewModelScope.launch {
            audioController.recordingVolumes.collect { volumes ->
                updateState { copy(recordingVolumes = volumes) }
            }
        }
    }

    private fun collectAudioSessionEffect() {
        viewModelScope.launch {
            audioController.audioSessionEffect.collect { effect ->
                sendEffect(AnalysisFlowUiEffect.ShowMessage(effect.toUiMessage()))
            }
        }
    }

    private fun AudioSessionEffect.toUiMessage(): AnalysisUiMessage =
        when (this) {
            AudioSessionEffect.RecordingStartFailed -> AnalysisUiMessage.RECORDING_START_FAILED
            AudioSessionEffect.RecordingStopFailed -> AnalysisUiMessage.RECORDING_STOP_FAILED
            AudioSessionEffect.PlaybackStartFailed -> AnalysisUiMessage.PLAYBACK_START_FAILED
        }

    override fun onCleared() {
        audioController.release()
        super.onCleared()
    }
}

private data class PresentationAnalysisSubmission(
    val name: String,
    val date: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val script: String?,
    val scriptFileUri: String?,
    val audioFileUri: String?,
    val recordingFilePath: String,
)

private fun AnalysisFlowUiIntent.reduceFormOrNull(form: AnalysisForm): AnalysisForm? =
    when (this) {
        is AnalysisFlowUiIntent.UpdatePresentationTitle -> form.copy(presentationTitle = title)
        is AnalysisFlowUiIntent.UpdatePresentationDate -> form.copy(presentationDate = date)
        is AnalysisFlowUiIntent.SelectScriptInputType -> form.copy(scriptInputType = inputType)
        is AnalysisFlowUiIntent.UpdateScript -> form.copy(script = script)
        is AnalysisFlowUiIntent.SelectScriptFile -> form.copy(scriptFileUri = fileUri)
        is AnalysisFlowUiIntent.SelectAudioFile -> form.copy(audioFileUri = fileUri)
        is AnalysisFlowUiIntent.SelectSituationOption -> form.selectSituationOption(option)
        else -> null
    }

private fun AnalysisForm.selectSituationOption(option: AnalysisSituationOption): AnalysisForm =
    when (option) {
        is AnalysisSituationOption.CategoryOption -> copy(category = option.category)
        is AnalysisSituationOption.PurposeOption -> copy(purpose = option.purpose)
        is AnalysisSituationOption.StyleOption -> copy(style = option.style)
        is AnalysisSituationOption.AudienceOption -> copy(audience = option.audience)
    }

private fun PresentationAnalysisSummary.toAnalysisForm(): AnalysisForm =
    AnalysisForm(
        presentationTitle = title,
        presentationDate = analyzedAt,
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
    )

private fun AnalysisFlowUiState.toPresentationAnalysisSubmissionOrNull(): PresentationAnalysisSubmission? {
    val category = form.category ?: return null
    val purpose = form.purpose ?: return null
    val style = form.style ?: return null
    val audience = form.audience ?: return null
    val recordingFilePath = form.audioFileUri ?: recordingState.recordingFilePath ?: return null
    val isFileUpload = form.scriptInputType == ScriptInputType.FILE_UPLOAD

    return PresentationAnalysisSubmission(
        name = form.presentationTitle.trim(),
        date = form.presentationDate,
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
        script = form.script.takeIf { !isFileUpload && it.isNotBlank() },
        scriptFileUri = form.scriptFileUri.takeIf { isFileUpload && !it.isNullOrBlank() },
        audioFileUri = form.audioFileUri,
        recordingFilePath = recordingFilePath,
    )
}

private fun String.toRequestDate(): String =
    runCatching {
        val (year, month, day) = split("년 ", "월 ", "일")

        LocalDate(
            year = year.toInt(),
            month = month.toInt(),
            day = day.toInt(),
        ).toString()
    }.getOrDefault(this)
