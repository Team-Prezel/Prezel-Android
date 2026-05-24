package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.practice.AnalyzePresentationRecordingUseCase
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationRecordingAnalysisResult
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
    private val analyzePresentationRecordingUseCase: AnalyzePresentationRecordingUseCase,
    private val analysisFileCache: AnalysisFileCache,
    private val audioController: RecordingAudioController,
) : BaseViewModel<AnalysisFlowUiState, AnalysisFlowUiIntent, AnalysisFlowUiEffect>(AnalysisFlowUiState()) {
    private var analyzeJob: Job? = null

    init {
        collectAudioSessionState()
        collectAudioSessionEffect()
    }

    override fun onIntent(intent: AnalysisFlowUiIntent) {
        when (intent) {
            is AnalysisFlowUiIntent.UpdatePresentationTitle -> updateForm { copy(presentationTitle = intent.title) }
            is AnalysisFlowUiIntent.UpdatePresentationDate -> updateForm { copy(presentationDate = intent.date) }
            is AnalysisFlowUiIntent.SelectSituationOption -> selectSituationOption(intent.option)
            is AnalysisFlowUiIntent.SelectScriptInputType -> updateForm { copy(scriptInputType = intent.inputType) }
            is AnalysisFlowUiIntent.UpdateScript -> updateForm { copy(script = intent.script) }
            is AnalysisFlowUiIntent.SelectScriptFile -> updateForm { copy(scriptFileUri = intent.fileUri) }
            is AnalysisFlowUiIntent.SelectAudioFile -> updateForm { copy(audioFileUri = intent.fileUri) }
            AnalysisFlowUiIntent.ClickRecordingControl -> handleRecordingControlClick()
            AnalysisFlowUiIntent.StopRecording -> audioController.stopRecording()
            AnalysisFlowUiIntent.ResetRecording -> audioController.reset()
            is AnalysisFlowUiIntent.RetryFileUpload -> retryFileUpload(intent.uploadType)
            AnalysisFlowUiIntent.Next -> moveNext()
            AnalysisFlowUiIntent.SkipScript -> skipScript()
            AnalysisFlowUiIntent.Back -> moveBack()
        }
    }

    private fun selectSituationOption(option: AnalysisSituationOption) {
        updateForm {
            when (option) {
                is AnalysisSituationOption.CategoryOption -> copy(category = option.category)
                is AnalysisSituationOption.PurposeOption -> copy(purpose = option.purpose)
                is AnalysisSituationOption.StyleOption -> copy(style = option.style)
                is AnalysisSituationOption.AudienceOption -> copy(audience = option.audience)
            }
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
                    AnalysisFlowStep.REPORT,
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
            submission
                .analyzePresentationRecording()
                .onSuccess { result ->
                    if (currentState.step == AnalysisFlowStep.ANALYZING) {
                        handleAnalysisSuccess(result)
                    }
                }.onFailure { throwable -> handleAnalysisFailure(throwable.toAnalysisFailureAction()) }
        }
    }

    private suspend fun PresentationAnalysisSubmission.analyzePresentationRecording(): Result<PresentationRecordingAnalysisResult> =
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

            analyzePresentationRecordingUseCase(
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

    private fun handleAnalysisSuccess(analysisResult: PresentationRecordingAnalysisResult) {
        updateState {
            copy(
                step = AnalysisFlowStep.REPORT,
                analysisResult = analysisResult,
            )
        }
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
            AnalysisUploadType.SCRIPT -> retryScriptFileUpload()
            AnalysisUploadType.AUDIO -> retryAudioUpload()
        }
    }

    private fun retryAudioUpload() {
        updateState {
            copy(
                step = AnalysisFlowStep.VOICE_RECORDING,
                form = form.copy(audioFileUri = null),
            )
        }
        audioController.reset()
    }

    private fun retryScriptFileUpload() {
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

    private fun skipScript() {
        if (currentState.step != AnalysisFlowStep.SCRIPT_INPUT) return

        updateState { copy(step = AnalysisFlowStep.VOICE_RECORDING) }
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
            AnalysisFlowStep.REPORT -> AnalysisFlowStep.VOICE_RECORDING
            AnalysisFlowStep.FILE_RECOGNITION_FAILED -> AnalysisFlowStep.VOICE_RECORDING
            AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED -> AnalysisFlowStep.SCRIPT_INPUT
        }

        if (previousStep == null) {
            viewModelScope.launch { sendEffect(AnalysisFlowUiEffect.NavigateBack) }
        } else {
            updateState { copy(step = previousStep) }
        }
    }

    private fun updateForm(reducer: AnalysisForm.() -> AnalysisForm) {
        updateState { copy(form = form.reducer()) }
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
