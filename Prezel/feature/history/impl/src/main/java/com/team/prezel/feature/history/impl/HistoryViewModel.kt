package com.team.prezel.feature.history.impl

import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HistoryViewModel @Inject constructor() : BaseViewModel<HistoryUiState, HistoryUiIntent, HistoryUiEffect>(HistoryUiState.Loading) {
    override fun onIntent(intent: HistoryUiIntent) {
        TODO("Not yet implemented")
    }
}
