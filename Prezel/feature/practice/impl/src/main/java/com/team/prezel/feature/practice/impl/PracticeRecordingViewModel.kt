package com.team.prezel.feature.practice.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.audio.AudioSessionEffect
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.RecordingAudioController
import com.team.prezel.core.domain.usecase.practice.FetchPracticeScriptUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiEffect
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiIntent
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiState
import com.team.prezel.feature.practice.impl.model.PracticeRecordingUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PracticeRecordingViewModel @Inject constructor(
    private val audioController: RecordingAudioController,
    private val fetchPracticeScriptUseCase: FetchPracticeScriptUseCase,
) : BaseViewModel<PracticeRecordingUiState, PracticeRecordingUiIntent, PracticeRecordingUiEffect>(PracticeRecordingUiState()) {
    init {
        collectAudioSessionState()
        collectAudioSessionEffect()
        fetchPracticeScript()
    }

    override fun onIntent(intent: PracticeRecordingUiIntent) {
        when (intent) {
            PracticeRecordingUiIntent.ClickRecordingControl -> handleRecordingControlClick()
        }
    }

    private fun handleRecordingControlClick() {
        when (currentState.recordingState) {
            AudioSessionState.Idle -> {
                if (currentState.practiceScript.isBlank()) {
                    showMessage(PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED)
                    return
                }
                audioController.startRecording()
            }

            is AudioSessionState.Recording -> audioController.stopRecording()
            is AudioSessionState.ReadyToPlay -> audioController.startPlayback()
            is AudioSessionState.Playing -> audioController.stopPlayback()
        }
    }

    private fun collectAudioSessionState() {
        viewModelScope.launch {
            audioController.audioSessionState.collect { audioState ->
                updateState {
                    copy(recordingState = audioState)
                }
            }
        }
    }

    private fun collectAudioSessionEffect() {
        viewModelScope.launch {
            audioController.audioSessionEffect.collect { effect ->
                showMessage(effect.toUiMessage())
            }
        }
    }

    private fun fetchPracticeScript() {
        viewModelScope.launch {
            fetchPracticeScriptUseCase()
                .onSuccess { script ->
                    updateState {
                        copy(practiceScript = script.content)
                    }
                }.onFailure {
                    showMessage(PracticeRecordingUiMessage.FETCH_PRACTICE_SCRIPT_FAILED)
                }
        }
    }

    private fun showMessage(message: PracticeRecordingUiMessage) {
        viewModelScope.launch {
            sendEffect(PracticeRecordingUiEffect.ShowMessage(message))
        }
    }

    private fun AudioSessionEffect.toUiMessage(): PracticeRecordingUiMessage =
        when (this) {
            AudioSessionEffect.RecordingStartFailed -> PracticeRecordingUiMessage.RECORDING_START_FAILED
            AudioSessionEffect.RecordingStopFailed -> PracticeRecordingUiMessage.RECORDING_STOP_FAILED
            AudioSessionEffect.PlaybackStartFailed -> PracticeRecordingUiMessage.PLAYBACK_START_FAILED
        }

    override fun onCleared() {
        audioController.release()
        super.onCleared()
    }
}
