package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class AnalysisFlowViewModel @Inject constructor(
    private val analyzePresentationRecordingUseCase: AnalyzePresentationRecordingUseCase,
    private val analysisFileCache: AnalysisFileCache,
) : BaseViewModel<AnalysisFlowUiState, AnalysisFlowUiIntent, AnalysisFlowUiEffect>(AnalysisFlowUiState()) {
    override fun onIntent(intent: AnalysisFlowUiIntent) {
        when (intent) {
            is AnalysisFlowUiIntent.UpdatePresentationTitle -> updateForm { copy(presentationTitle = intent.title) }
            is AnalysisFlowUiIntent.UpdatePresentationDate -> updateForm { copy(presentationDate = intent.date) }
            is AnalysisFlowUiIntent.SelectSituationOption -> selectSituationOption(intent.option)
            is AnalysisFlowUiIntent.SelectScriptInputType -> updateForm { copy(scriptInputType = intent.inputType) }
            is AnalysisFlowUiIntent.UpdateScript -> updateForm { copy(script = intent.script) }
            is AnalysisFlowUiIntent.SelectScriptFile -> updateForm { copy(scriptFileUri = intent.fileUri) }
            is AnalysisFlowUiIntent.SelectAudioFile -> updateForm { copy(audioFileUri = intent.fileUri) }
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

        if (currentState.step == AnalysisFlowStep.AUDIO_UPLOAD) {
            analyzePresentation()
            return
        }

        updateState {
            copy(
                step = when (step) {
                    AnalysisFlowStep.PRESENTATION_SCHEDULE -> AnalysisFlowStep.PRESENTATION_SITUATION
                    AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisFlowStep.SCRIPT_INPUT
                    AnalysisFlowStep.SCRIPT_INPUT -> AnalysisFlowStep.AUDIO_UPLOAD
                    AnalysisFlowStep.AUDIO_UPLOAD,
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
        val submission = currentState.form.toPresentationAnalysisSubmissionOrNull() ?: return

        updateState { copy(step = AnalysisFlowStep.ANALYZING) }

        viewModelScope.launch {
            submission
                .analyzePresentationRecording()
                .onSuccess(::handleAnalysisSuccess)
                .onFailure { throwable -> handleAnalysisFailure(throwable.toAnalysisFailureAction()) }
        }
    }

    private suspend fun PresentationAnalysisSubmission.analyzePresentationRecording(): Result<PresentationRecordingAnalysisResult> =
        runCatching {
            val audioFile = analysisFileCache.copyUriToCache(
                uriString = audioFileUri,
                prefix = "audio",
            )
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
                audioFilePath = audioFile.absolutePath,
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
                updateState { copy(step = AnalysisFlowStep.AUDIO_UPLOAD) }
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
                step = AnalysisFlowStep.AUDIO_UPLOAD,
                form = form.copy(audioFileUri = null),
            )
        }
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

        updateState { copy(step = AnalysisFlowStep.AUDIO_UPLOAD) }
    }

    private fun moveBack() {
        val previousStep = when (currentState.step) {
            AnalysisFlowStep.PRESENTATION_SCHEDULE -> null
            AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisFlowStep.PRESENTATION_SCHEDULE
            AnalysisFlowStep.SCRIPT_INPUT -> AnalysisFlowStep.PRESENTATION_SITUATION
            AnalysisFlowStep.AUDIO_UPLOAD -> AnalysisFlowStep.SCRIPT_INPUT
            AnalysisFlowStep.ANALYZING -> AnalysisFlowStep.AUDIO_UPLOAD
            AnalysisFlowStep.REPORT -> AnalysisFlowStep.AUDIO_UPLOAD
            AnalysisFlowStep.FILE_RECOGNITION_FAILED -> AnalysisFlowStep.AUDIO_UPLOAD
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
    val audioFileUri: String,
)

private fun AnalysisForm.toPresentationAnalysisSubmissionOrNull(): PresentationAnalysisSubmission? {
    val category = category ?: return null
    val purpose = purpose ?: return null
    val style = style ?: return null
    val audience = audience ?: return null
    val audioFileUri = audioFileUri ?: return null

    return PresentationAnalysisSubmission(
        name = presentationTitle.trim(),
        date = presentationDate,
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
        script = script.takeIf(String::isNotBlank),
        scriptFileUri = scriptFileUri.takeIf { scriptInputType == ScriptInputType.FILE_UPLOAD },
        audioFileUri = audioFileUri,
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
