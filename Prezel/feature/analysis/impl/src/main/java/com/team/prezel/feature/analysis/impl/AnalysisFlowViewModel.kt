package com.team.prezel.feature.analysis.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiEffect
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
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
                is AnalysisFlowUiIntent.SelectCategory -> updateForm { copy(category = intent.category) }
                is AnalysisFlowUiIntent.SelectPurpose -> updateForm { copy(purpose = intent.purpose) }
                is AnalysisFlowUiIntent.SelectStyle -> updateForm { copy(style = intent.style) }
                is AnalysisFlowUiIntent.SelectAudience -> updateForm { copy(audience = intent.audience) }
                is AnalysisFlowUiIntent.SelectScriptInputType -> updateForm { copy(scriptInputType = intent.inputType) }
                is AnalysisFlowUiIntent.UpdateScript -> updateForm { copy(script = intent.script) }
                is AnalysisFlowUiIntent.SelectScriptFile -> updateForm { copy(scriptFileUri = intent.fileUri) }
                is AnalysisFlowUiIntent.SelectAudioFile -> updateForm { copy(audioFileUri = intent.fileUri) }
                AnalysisFlowUiIntent.Next -> moveNext()
                AnalysisFlowUiIntent.SkipScript -> skipScript()
                AnalysisFlowUiIntent.Back -> moveBack()
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
                        AnalysisFlowStep.ANALYZING -> AnalysisFlowStep.ANALYZING
                    },
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
