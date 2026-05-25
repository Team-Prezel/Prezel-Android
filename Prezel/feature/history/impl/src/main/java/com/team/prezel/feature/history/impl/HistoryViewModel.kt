package com.team.prezel.feature.history.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchPastPresentationsUseCase
import com.team.prezel.core.domain.usecase.presentation.FetchUpcomingPresentationsUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryPageType
import com.team.prezel.feature.history.impl.model.HistoryPageUiModel
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import com.team.prezel.feature.history.impl.model.HistoryUiModel.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    private val fetchUpcomingPresentationsUseCase: FetchUpcomingPresentationsUseCase,
    private val fetchPastPresentationsUseCase: FetchPastPresentationsUseCase,
) : BaseViewModel<HistoryUiState, HistoryUiIntent, HistoryUiEffect>(HistoryUiState.Loading) {
    override fun onIntent(intent: HistoryUiIntent) {
        when (intent) {
            HistoryUiIntent.FetchData -> fetchData()
            is HistoryUiIntent.ClickItem -> handleClickItem(presentationId = intent.presentationId)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            val upcomingDeferred = async { fetchUpcomingPresentationsUseCase() }
            val pastDeferred = async { fetchPastPresentationsUseCase() }

            val upcoming = upcomingDeferred.await().fold(
                onSuccess = { infos -> infos.map { info -> info.toUiModel() } },
                onFailure = {
                    sendEffect(HistoryUiEffect.ShowMessage(HistoryUiMessage.FETCH_DATA_FAILED))
                    listOf()
                },
            )
            val past = pastDeferred.await().fold(
                onSuccess = { infos -> infos.map { info -> info.toUiModel() } },
                onFailure = {
                    sendEffect(HistoryUiEffect.ShowMessage(HistoryUiMessage.FETCH_DATA_FAILED))
                    listOf()
                },
            )

            updateState {
                HistoryUiState.Content(
                    pages = persistentListOf(
                        HistoryPageUiModel(type = HistoryPageType.PREPARING, items = upcoming.toImmutableList()),
                        HistoryPageUiModel(type = HistoryPageType.COMPLETED, items = past.toImmutableList()),
                    ),
                )
            }
        }
    }

    private fun handleClickItem(presentationId: Long) {
        val pastItems = (currentState as? HistoryUiState.Content)?.currentPageItem(type = HistoryPageType.COMPLETED).orEmpty()
        val isPast = pastItems.any { history -> history.id == presentationId }

        viewModelScope.launch {
            sendEffect(HistoryUiEffect.NavigateToReport(presentationId = presentationId, isPast = isPast))
        }
    }
}
