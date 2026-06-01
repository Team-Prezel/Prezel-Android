package com.team.prezel.feature.badge.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.badge.FetchBadgeDetailUseCase
import com.team.prezel.core.domain.usecase.badge.FetchBadgesUseCase
import com.team.prezel.core.model.badge.Badge
import com.team.prezel.core.model.badge.BadgeDetail
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.badge.impl.contract.BadgeUiEffect
import com.team.prezel.feature.badge.impl.contract.BadgeUiIntent
import com.team.prezel.feature.badge.impl.contract.BadgeUiState
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel
import com.team.prezel.feature.badge.impl.model.BadgeUiMessage
import com.team.prezel.feature.badge.impl.model.BadgeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BadgeViewModel @Inject constructor(
    private val fetchBadgesUseCase: FetchBadgesUseCase,
    private val fetchBadgeDetailUseCase: FetchBadgeDetailUseCase,
) : BaseViewModel<BadgeUiState, BadgeUiIntent, BadgeUiEffect>(BadgeUiState()) {
    private val badgeDetailCache = mutableMapOf<String, BadgeDetailUiModel>()

    init {
        fetchData()
    }

    override fun onIntent(intent: BadgeUiIntent) {
        when (intent) {
            is BadgeUiIntent.ClickBadge -> fetchBadgeDetail(intent.badgeCode)
            BadgeUiIntent.DismissBadgeDetail -> dismissBadgeDetail()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            fetchBadgesUseCase()
                .onSuccess { badges -> handleFetchDataSuccess(badges = badges) }
                .onFailure {
                    updateState { copy(isLoading = false) }
                    sendEffect(BadgeUiEffect.ShowMessage(BadgeUiMessage.FETCH_DATA_FAILED))
                }
        }
    }

    private fun handleFetchDataSuccess(badges: List<Badge>) {
        updateState {
            copy(
                isLoading = false,
                badges = badges.map { badge -> badge.toUiModel() }.toImmutableList(),
            )
        }
    }

    private fun fetchBadgeDetail(badgeCode: String) {
        val selectedBadge = currentState.badges.firstOrNull { badge -> badge.badgeCode == badgeCode } ?: return
        val cachedDetail = badgeDetailCache[badgeCode]

        updateState {
            copy(
                selectedBadgeCode = selectedBadge.badgeCode,
                selectedBadgeDetail = cachedDetail,
            )
        }

        if (cachedDetail != null) return

        viewModelScope.launch {
            fetchBadgeDetailUseCase(badgeCode = badgeCode)
                .onSuccess { detail ->
                    val detailUiModel = detail.toUiModel()
                    badgeDetailCache[badgeCode] = detailUiModel

                    if (currentState.selectedBadgeCode == badgeCode) {
                        updateState { copy(selectedBadgeDetail = detailUiModel) }
                    }
                }.onFailure {
                    if (currentState.selectedBadgeCode == badgeCode) {
                        dismissBadgeDetail()
                        sendEffect(BadgeUiEffect.ShowMessage(BadgeUiMessage.FETCH_BADGE_DETAIL_FAILED))
                    }
                }
        }
    }

    private fun dismissBadgeDetail() {
        updateState {
            copy(
                selectedBadgeCode = null,
                selectedBadgeDetail = null,
            )
        }
    }

    private fun Badge.toUiModel(): BadgeUiModel =
        BadgeUiModel(
            badgeCode = badgeCode,
            badgeName = badgeName,
            imageUrl = imageUrl,
            isUnlocked = isUnlocked,
        )

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
