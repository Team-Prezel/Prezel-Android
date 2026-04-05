package com.team.prezel.feature.home.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState
import com.team.prezel.feature.home.impl.model.PresentationUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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

    companion object {
        fun from(
            presentations: List<PresentationUiModel>,
            nickname: String,
        ): HomeUiState =
            when (presentations.size) {
                0 -> Empty(nickname = nickname)
                1 -> SingleContent(presentation = presentations.first())
                else -> MultipleContent(presentations = presentations.toImmutableList())
            }
    }
}
