package com.team.prezel.feature.history.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryPresentationStatus
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
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
                HistoryUiState.Content(
                    preparingPresentations = persistentListOf(
                        HistoryUiModel(
                            id = 1L,
                            dDayLabel = "D-5",
                            dateLabel = "2026.04.19",
                            title = "캡스톤서비스기획 중간고사 발표",
                            category = Category.EDUCATION,
                            purpose = Purpose.CONTENT_DELIVERY,
                            style = Style.PROFESSIONAL,
                            audience = Audience.EXPERT,
                            status = HistoryPresentationStatus.PREPARING,
                        ),
                        HistoryUiModel(
                            id = 2L,
                            dDayLabel = "D-7",
                            dateLabel = "2026.04.21",
                            title = "IT동아리 대규모 세미나",
                            category = Category.REPORT,
                            purpose = Purpose.CONTENT_DELIVERY,
                            style = Style.FRIENDLY,
                            audience = Audience.GENERAL_AUDIENCE,
                            status = HistoryPresentationStatus.PREPARING,
                        ),
                    ),
                    completedPresentations = persistentListOf(
                        HistoryUiModel(
                            id = 3L,
                            dDayLabel = "D+1",
                            dateLabel = "2026.04.12",
                            title = "서비스 런칭 회고 발표",
                            category = Category.REPORT,
                            purpose = Purpose.IMPROVE_UNDERSTANDING,
                            style = Style.CALM,
                            audience = Audience.TEAMMATES,
                            status = HistoryPresentationStatus.COMPLETED,
                        ),
                        HistoryUiModel(
                            id = 4L,
                            dDayLabel = "D+12",
                            dateLabel = "2026.04.02",
                            title = "졸업 프로젝트 최종 발표",
                            category = Category.EDUCATION,
                            purpose = Purpose.BUILD_EMPATHY,
                            style = Style.COMFORTABLE,
                            audience = Audience.EXPERT,
                            status = HistoryPresentationStatus.COMPLETED,
                        ),
                    ),
                )
            }
        }
    }
}
