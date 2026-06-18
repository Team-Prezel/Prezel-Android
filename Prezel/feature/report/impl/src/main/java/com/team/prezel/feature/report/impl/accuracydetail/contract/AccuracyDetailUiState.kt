package com.team.prezel.feature.report.impl.accuracydetail.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.PresentationWordDetail
import com.team.prezel.core.ui.base.UiState

@Immutable
internal sealed interface AccuracyDetailUiState : UiState {
    data object Loading : AccuracyDetailUiState

    data object Error : AccuracyDetailUiState

    data class Content(
        val wordDetail: PresentationWordDetail,
    ) : AccuracyDetailUiState
}
