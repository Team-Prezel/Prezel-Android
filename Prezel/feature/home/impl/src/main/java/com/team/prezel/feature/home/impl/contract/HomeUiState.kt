package com.team.prezel.feature.home.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState
import com.team.prezel.feature.home.impl.model.PresentationUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface HomeUiState : UiState {
    data object Loading : HomeUiState

    data class Empty(
        val nickname: String,
    ) : HomeUiState

    data class SingleContent(
        val presentation: PresentationUiModel,
    ) : HomeUiState

    data class MultipleContent(
        val presentations: ImmutableList<PresentationUiModel>,
    ) : HomeUiState
}
