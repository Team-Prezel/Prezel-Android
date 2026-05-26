package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.presentation.AnalyzePresentationUseCase
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
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

        updateState {
            copy(
                step = when (step) {
                    AnalysisFlowStep.PRESENTATION_SCHEDULE -> AnalysisFlowStep.PRESENTATION_SITUATION
                    AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisFlowStep.SCRIPT_INPUT
                    AnalysisFlowStep.SCRIPT_INPUT -> AnalysisFlowStep.VOICE_RECORDING
                    AnalysisFlowStep.AUDIO_UPLOAD,
                    AnalysisFlowStep.VOICE_RECORDING,
                    AnalysisFlowStep.ANALYZING,
                    AnalysisFlowStep.FILE_RECOGNITION_FAILED,
                    AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
                    -> step
                },
            )
        }
    }

    private fun analyzePresentation() {
        val submission = currentState.toPresentationAnalysisSubmissionOrNull() ?: return

        updateState { copy(step = AnalysisFlowStep.ANALYZING) }

        analyzeJob?.cancel()
        analyzeJob = viewModelScope.launch {
            val analysisResult = submission.analyzePresentationRecording()

            analysisResult.fold(
                onSuccess = { result ->
                    if (currentState.step == AnalysisFlowStep.ANALYZING) {
                        sendEffect(AnalysisFlowUiEffect.NavigateToReport(presentationId = result))
                    }
                },
                onFailure = { throwable ->
                    handleAnalysisFailure(throwable.toAnalysisFailureAction())
                },
            )
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

    private fun handleAnalysisFailure(action: AnalysisFailureAction) {
        when (action) {
            is AnalysisFailureAction.RetryFileUpload -> {
                updateState {
                    copy(
                        step = when (action.uploadType) {
                            AnalysisUploadType.AUDIO -> AnalysisFlowStep.FILE_RECOGNITION_FAILED
                            AnalysisUploadType.SCRIPT -> AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED
                        },
                    )
                }
            }

            is AnalysisFailureAction.ShowMessage -> {
                viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.ShowMessage(action.message)) }
                updateState { copy(step = AnalysisFlowStep.VOICE_RECORDING) }
            }
        }
    }

    private fun retryFileUpload(uploadType: AnalysisUploadType) {
        when (uploadType) {
            AnalysisUploadType.SCRIPT -> {
                updateState {
                    copy(
                        step = AnalysisFlowStep.SCRIPT_INPUT,
                        form = form.copy(
                            scriptInputType = ScriptInputType.FILE_UPLOAD,
                            scriptFileUri = null,
                        ),
                    )
                }
            }

            AnalysisUploadType.AUDIO -> {
                updateState {
                    copy(
                        step = AnalysisFlowStep.VOICE_RECORDING,
                        form = form.copy(audioFileUri = null),
                    )
                }
                audioController.reset()
            }
        }
    }

    private fun skipScript() {
        if (currentState.step != AnalysisFlowStep.SCRIPT_INPUT) return

        updateState {
            copy(
                step = AnalysisFlowStep.VOICE_RECORDING,
                form = form.copy(
                    script = "",
                    scriptFileUri = null,
                ),
            )
        }
    }

    private fun moveBack() {
        if (currentState.step == AnalysisFlowStep.ANALYZING) analyzeJob?.cancel()

        val previousStep = when (currentState.step) {
            AnalysisFlowStep.PRESENTATION_SCHEDULE -> null
            AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisFlowStep.PRESENTATION_SCHEDULE
            AnalysisFlowStep.SCRIPT_INPUT -> AnalysisFlowStep.PRESENTATION_SITUATION
            AnalysisFlowStep.AUDIO_UPLOAD -> AnalysisFlowStep.SCRIPT_INPUT
            AnalysisFlowStep.VOICE_RECORDING -> AnalysisFlowStep.SCRIPT_INPUT
            AnalysisFlowStep.ANALYZING -> AnalysisFlowStep.VOICE_RECORDING
            AnalysisFlowStep.FILE_RECOGNITION_FAILED -> AnalysisFlowStep.VOICE_RECORDING
            AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED -> AnalysisFlowStep.SCRIPT_INPUT
        }

        if (previousStep == null) {
            viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateBack) }
        } else {
            updateState { copy(step = previousStep) }
        }
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
