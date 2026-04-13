package com.team.prezel.feature.history.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryChipUiModel
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
                            category = Category.EDUCATION,
                            dDayLabel = "D-5",
                            dateLabel = "2026.04.19",
                            title = "캡스톤서비스기획 중간고사 발표",
                            chips = persistentListOf(
                                HistoryChipUiModel(label = "학술·교육", highlighted = true),
                                HistoryChipUiModel(label = "내용 전달"),
                                HistoryChipUiModel(label = "논리적"),
                                HistoryChipUiModel(label = "전문가"),
                            ),
                        ),
                        HistoryUiModel(
                            id = 2L,
                            category = Category.REPORT,
                            dDayLabel = "D-7",
                            dateLabel = "2026.04.21",
                            title = "IT동아리 대규모 세미나",
                            chips = persistentListOf(
                                HistoryChipUiModel(label = "업무·보고", highlighted = true),
                                HistoryChipUiModel(label = "내용 전달"),
                                HistoryChipUiModel(label = "논리적"),
                                HistoryChipUiModel(label = "일반 청중"),
                            ),
                        ),
                    ),
                    completedPresentations = persistentListOf(
                        HistoryUiModel(
                            id = 3L,
                            category = Category.REPORT,
                            dDayLabel = "D+1",
                            dateLabel = "2026.04.12",
                            title = "서비스 런칭 회고 발표",
                            chips = persistentListOf(
                                HistoryChipUiModel(label = "업무·보고", highlighted = true),
                                HistoryChipUiModel(label = "분석형"),
                                HistoryChipUiModel(label = "팀 회고"),
                            ),
                        ),
                        HistoryUiModel(
                            id = 4L,
                            category = Category.EDUCATION,
                            dDayLabel = "D+12",
                            dateLabel = "2026.04.02",
                            title = "졸업 프로젝트 최종 발표",
                            chips = persistentListOf(
                                HistoryChipUiModel(label = "학술·교육", highlighted = true),
                                HistoryChipUiModel(label = "스토리텔링"),
                                HistoryChipUiModel(label = "전문가"),
                            ),
                        ),
                    ),
                )
            }
        }
    }
}
