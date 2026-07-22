package com.team.prezel.feature.badge.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.badge.FetchBadgeDetailUseCase
import com.team.prezel.core.model.badge.BadgeDetail
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.badge.impl.contract.BadgeUiEffect
import com.team.prezel.feature.badge.impl.contract.BadgeUiIntent
import com.team.prezel.feature.badge.impl.contract.BadgeUiState
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel
import com.team.prezel.feature.badge.impl.model.BadgeUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BadgeViewModel @Inject constructor(
    private val fetchBadgeDetailUseCase: FetchBadgeDetailUseCase,
) : BaseViewModel<BadgeUiState, BadgeUiIntent, BadgeUiEffect>(BadgeUiState()) {
    private val badgeDetailCache = mutableMapOf<String, BadgeDetailUiModel>()

    override fun onIntent(intent: BadgeUiIntent) {
        when (intent) {
            is BadgeUiIntent.FetchBadgeDetail -> fetchBadgeDetail(intent.badgeCode)
        }
    }

    private fun fetchBadgeDetail(badgeCode: String) {
        val cachedDetail = badgeDetailCache[badgeCode]

        if (cachedDetail != null) {
            updateState { copy(isLoading = false, badgeDetail = cachedDetail) }
            return
        }

        updateState { copy(isLoading = true, badgeDetail = null) }

        viewModelScope.launch {
            fetchBadgeDetailUseCase(badgeCode = badgeCode)
                .onSuccess { detail ->
                    val detailUiModel = detail.toUiModel()
                    badgeDetailCache[badgeCode] = detailUiModel
                    updateState { copy(isLoading = false, badgeDetail = detailUiModel) }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    sendEffect(BadgeUiEffect.ShowMessage(BadgeUiMessage.FETCH_BADGE_DETAIL_FAILED))
                }
        }
    }

    private fun BadgeDetail.toUiModel(): BadgeDetailUiModel =
        BadgeDetailUiModel(
            badgeCode = badgeCode,
            badgeName = badgeName,
            conditionText = conditionText,
            detailDescription = detailDescription.replace(".", ".\n"),
            imageUrl = imageUrl,
            isUnlocked = isUnlocked,
        )
}
