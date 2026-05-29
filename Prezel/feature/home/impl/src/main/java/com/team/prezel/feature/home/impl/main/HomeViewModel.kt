package com.team.prezel.feature.home.impl.main

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchMainDataUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.main.contract.HomeUiEffect
import com.team.prezel.feature.home.impl.main.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.main.contract.HomeUiState
import com.team.prezel.feature.home.impl.main.contract.HomeUiState.Companion.toUiState
import com.team.prezel.feature.home.impl.main.model.HomeUiMessage
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val fetchMainDataUseCase: FetchMainDataUseCase,
) : BaseViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(HomeUiState.Loading) {
    override fun onIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.FetchData -> fetchData()
            is HomeUiIntent.ClickCardGraphItem -> updateCardGraphData(intent.presentationId, intent.index)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchMainDataUseCase()
                .onSuccess { data -> updateState { data.toUiState() } }
                .onFailure { sendEffect(HomeUiEffect.ShowMessage(HomeUiMessage.FETCH_DATA_FAILED)) }
        }
    }

    private fun updateCardGraphData(
        presentationId: Long,
        index: Int,
    ) {
        updateState {
            when (this) {
                HomeUiState.Loading, is HomeUiState.Empty -> this
                is HomeUiState.SingleContent -> {
                    val currentPresentation = presentation as? PresentationUiModel.Past ?: return@updateState this
                    if (currentPresentation.id != presentationId) return@updateState this

                    copy(
                        presentation = currentPresentation.copy(
                            growthGraphData = currentPresentation.growthGraphData.copy(
                                selectedItemIndex = currentPresentation.growthGraphData.selectedItemIndex.toggle(index),
                            ),
                        ),
                    )
                }

                is HomeUiState.MultipleContent -> {
                    copy(
                        presentations = presentations
                            .map { presentation ->
                                val currentPresentation = presentation as? PresentationUiModel.Past ?: return@map presentation
                                if (currentPresentation.id != presentationId) return@map presentation

                                currentPresentation.copy(
                                    growthGraphData = currentPresentation.growthGraphData.copy(
                                        selectedItemIndex = currentPresentation.growthGraphData.selectedItemIndex.toggle(index),
                                    ),
                                )
                            }.toImmutableList(),
                    )
                }
            }
        }
    }

    private fun Int?.toggle(index: Int): Int? = if (this == index) null else index
}
