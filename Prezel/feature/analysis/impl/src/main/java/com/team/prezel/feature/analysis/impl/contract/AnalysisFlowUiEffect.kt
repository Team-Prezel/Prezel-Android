package com.team.prezel.feature.analysis.impl.contract

import com.team.prezel.core.ui.base.UiEffect

internal sealed interface AnalysisFlowUiEffect : UiEffect {
    data object NavigateBack : AnalysisFlowUiEffect
}
