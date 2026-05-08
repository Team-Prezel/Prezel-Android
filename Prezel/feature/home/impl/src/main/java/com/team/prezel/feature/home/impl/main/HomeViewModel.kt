package com.team.prezel.feature.home.impl.main

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Presentation
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.home.impl.main.contract.HomeUiEffect
import com.team.prezel.feature.home.impl.main.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.main.contract.HomeUiState
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel.Companion.toUiModel
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

    private fun getPresentations(): List<PresentationUiModel> =
        listOf(
            Presentation(
                id = 1L,
                title = "신규 서비스 제안 발표",
                date = LocalDate(2026, 4, 10),
                category = Category.PERSUASION,
                purpose = Purpose.CONTENT_DELIVERY,
                style = Style.PROFESSIONAL,
                audience = Audience.GENERAL_AUDIENCE,
            ),
            Presentation(
                id = 2L,
                title = "주간 업무 공유",
                date = LocalDate(2026, 4, 12),
                category = Category.REPORT,
                purpose = Purpose.CONTENT_DELIVERY,
                style = Style.PROFESSIONAL,
                audience = Audience.GENERAL_AUDIENCE,
            ),
        ).map { presentation -> presentation.toUiModel() }
}
