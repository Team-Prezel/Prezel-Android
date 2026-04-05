package com.team.prezel.feature.home.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.home.impl.contract.HomeUiEffect
import com.team.prezel.feature.home.impl.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.contract.HomeUiState
import com.team.prezel.feature.home.impl.model.CategoryUiModel
import com.team.prezel.feature.home.impl.model.PresentationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : BaseViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(HomeUiState.Loading) {
    override fun onIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            val nickname = "프레즐"
            val presentations = getPresentations()

            updateState {
                HomeUiState.from(
                    presentations = presentations,
                    nickname = nickname,
                )
            }
        }
    }

    // TODO: repository 연결 후 실제 홈 데이터를 가져오도록 교체
    private fun getPresentations(): List<PresentationUiModel> =
        listOf(
            PresentationUiModel(
                id = 1L,
                category = CategoryUiModel.PERSUASION,
                title = "신규 서비스 제안 발표",
                date = LocalDate(2026, 4, 10),
            ),
            PresentationUiModel(
                id = 2L,
                category = CategoryUiModel.REPORT,
                title = "주간 업무 공유",
                date = LocalDate(2026, 4, 12),
            ),
        )
}
