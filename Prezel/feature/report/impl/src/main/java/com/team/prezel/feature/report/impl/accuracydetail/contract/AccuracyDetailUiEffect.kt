package com.team.prezel.feature.report.impl.accuracydetail.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.report.impl.accuracydetail.model.AccuracyDetailUiMessage

internal sealed interface AccuracyDetailUiEffect : UiEffect {
    data class ShowMessage(
        val message: AccuracyDetailUiMessage,
    ) : AccuracyDetailUiEffect
}
