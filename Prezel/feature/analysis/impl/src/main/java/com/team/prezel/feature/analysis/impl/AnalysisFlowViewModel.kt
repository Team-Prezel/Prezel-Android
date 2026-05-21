package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.base.BaseViewModel
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
import javax.inject.Inject

@HiltViewModel
internal class AnalysisFlowViewModel @Inject constructor() :
    BaseViewModel<AnalysisFlowUiState, AnalysisFlowUiIntent, AnalysisFlowUiEffect>(AnalysisFlowUiState()) {
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
            if (!currentState.canMoveNext && currentState.step != AnalysisFlowStep.ANALYZING) return

            updateState {
                copy(
                    step = when (step) {
                        AnalysisFlowStep.PRESENTATION_SCHEDULE -> AnalysisFlowStep.PRESENTATION_SITUATION
                        AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisFlowStep.SCRIPT_INPUT
                        AnalysisFlowStep.SCRIPT_INPUT -> AnalysisFlowStep.AUDIO_UPLOAD
                        AnalysisFlowStep.AUDIO_UPLOAD -> AnalysisFlowStep.ANALYZING
                        AnalysisFlowStep.ANALYZING -> AnalysisFlowStep.REPORT
                        AnalysisFlowStep.REPORT,
                        AnalysisFlowStep.FILE_RECOGNITION_FAILED,
                        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
                        -> AnalysisFlowStep.REPORT
                    },
                )
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
