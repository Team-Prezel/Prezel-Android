package com.team.prezel.feature.home.impl.main.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.MainDataBundle
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel.Companion.toUiModel
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
    ) : HomeUiState {
        val dDayLabels: ImmutableList<String> = presentations.map(PresentationUiModel::dDay).toImmutableList()
    }

    fun presentationCount(): Int =
        when (this) {
            Loading -> 0
            is Empty -> 1
            is SingleContent -> 1
            is MultipleContent -> presentations.size
        }

    companion object {
        fun MainDataBundle.toUiState(): HomeUiState {
            val uiModels = presentations.map { data -> data.toUiModel() }
            val fallbackNickname = nickname.ifBlank { "unknown" }

            return when (presentations.size) {
                0 -> Empty(nickname = fallbackNickname)
                1 -> SingleContent(presentation = uiModels.first())
                else -> MultipleContent(presentations = uiModels.toImmutableList())
            }
        }
    }
}
