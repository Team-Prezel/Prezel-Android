package com.team.prezel.feature.report.impl

import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.impl.contract.ReportUiEffect
import com.team.prezel.feature.report.impl.contract.ReportUiIntent
import com.team.prezel.feature.report.impl.contract.ReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ReportViewModel @Inject constructor() : BaseViewModel<ReportUiState, ReportUiIntent, ReportUiEffect>(ReportUiState.Loading) {
    init {
        updateState { ReportUiState.Content }
    }

    override fun onIntent(intent: ReportUiIntent) = Unit
}
