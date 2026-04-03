package com.team.prezel.feature.home.impl

import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.home.impl.contract.HomeUiEffect
import com.team.prezel.feature.home.impl.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.contract.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : BaseViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(HomeUiState.Loading) {
    override fun onIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.AA -> {}
        }
    }
}
