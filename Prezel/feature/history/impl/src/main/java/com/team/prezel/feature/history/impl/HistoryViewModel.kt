package com.team.prezel.feature.history.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Presentation
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryPageType
import com.team.prezel.feature.history.impl.model.HistoryPageUiModel
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class HistoryViewModel @Inject constructor() : BaseViewModel<HistoryUiState, HistoryUiIntent, HistoryUiEffect>(HistoryUiState.Loading) {
    override fun onIntent(intent: HistoryUiIntent) {
        when (intent) {
            HistoryUiIntent.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateState {
                createHistoryContentState()
            }
        }
    }

    private fun createHistoryContentState(): HistoryUiState.Content =
        historyPresentations
            .map(HistoryUiModel::toUiModel)
            .let { uiModels ->
                HistoryUiState.Content(
                    pages = persistentListOf(
                        HistoryPageUiModel(
                            type = HistoryPageType.PREPARING,
                            items = uiModels.filter(HistoryUiModel::isPreparing).toImmutableList(),
                        ),
                        HistoryPageUiModel(
                            type = HistoryPageType.COMPLETED,
                            items = uiModels.filterNot(HistoryUiModel::isPreparing).toImmutableList(),
                        ),
                    ),
                )
            }

    private val historyPresentations = persistentListOf(
        Presentation(
            id = 1L,
            title = "캡스톤서비스기획 중간고사 발표",
            date = LocalDate(2026, 4, 19),
            category = Category.EDUCATION,
            purpose = Purpose.CONTENT_DELIVERY,
            style = Style.PROFESSIONAL,
            audience = Audience.EXPERT,
        ),
        Presentation(
            id = 2L,
            title = "IT동아리 대규모 세미나",
            date = LocalDate(2026, 4, 21),
            category = Category.REPORT,
            purpose = Purpose.CONTENT_DELIVERY,
            style = Style.FRIENDLY,
            audience = Audience.GENERAL_AUDIENCE,
        ),
        Presentation(
            id = 3L,
            title = "서비스 런칭 회고 발표",
            date = LocalDate(2026, 4, 12),
            category = Category.REPORT,
            purpose = Purpose.IMPROVE_UNDERSTANDING,
            style = Style.CALM,
            audience = Audience.TEAMMATES,
        ),
        Presentation(
            id = 4L,
            title = "졸업 프로젝트 최종 발표",
            date = LocalDate(2026, 4, 2),
            category = Category.EDUCATION,
            purpose = Purpose.BUILD_EMPATHY,
            style = Style.COMFORTABLE,
            audience = Audience.EXPERT,
        ),
    )
}
