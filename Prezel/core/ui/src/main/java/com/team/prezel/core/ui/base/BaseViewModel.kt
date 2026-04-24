package com.team.prezel.core.ui.base

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@Immutable
interface UiState

interface UiIntent

interface UiEffect

abstract class BaseViewModel<STATE : UiState, INTENT : UiIntent, EFFECT : UiEffect>(
    initialState: STATE,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState

    private val _uiEffect = Channel<EFFECT>()
    val uiEffect: Flow<EFFECT> = _uiEffect.receiveAsFlow()

    protected val currentState: STATE
        get() = uiState.value

    protected fun updateState(reducer: STATE.() -> STATE) {
        _uiState.update(reducer)
    }

    protected suspend fun sendEffect(effect: EFFECT) {
        _uiEffect.send(effect)
    }

    abstract fun onIntent(intent: INTENT)
}
