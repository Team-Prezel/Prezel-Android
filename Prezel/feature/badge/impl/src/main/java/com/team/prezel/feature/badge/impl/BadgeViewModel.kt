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
import com.team.prezel.feature.badge.impl.model.BadgeUiMessage
import com.team.prezel.feature.badge.impl.model.BadgeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BadgeViewModel @Inject constructor(
    private val fetchBadgesUseCase: FetchBadgesUseCase,
    private val fetchBadgeDetailUseCase: FetchBadgeDetailUseCase,
) : BaseViewModel<BadgeUiState, BadgeUiIntent, BadgeUiEffect>(BadgeUiState()) {
    init {
        fetchData()
    }

    override fun onIntent(intent: BadgeUiIntent) {
        when (intent) {
            is BadgeUiIntent.ClickBadge -> {}
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchBadgesUseCase()
                .onSuccess { badges -> handleFetchDataSuccess(badges = badges) }
                .onFailure { sendEffect(BadgeUiEffect.ShowMessage(BadgeUiMessage.FETCH_DATA_FAILED)) }
        }
    }

    private suspend fun handleFetchDataSuccess(badges: List<Badge>) =
        coroutineScope {
            val badgeDetails = badges
                .map { badge -> async { fetchBadgeDetailUseCase(badgeCode = badge.badgeCode) } }
                .awaitAll()
                .mapNotNull(Result<BadgeDetail>::getOrNull)
                .map { detail ->
                    with(detail) {
                        BadgeUiModel(
                            badgeCode = badgeCode,
                            badgeName = badgeName,
                            conditionText = conditionText,
                            detailDescription = detailDescription,
                            imageUrl = imageUrl,
                            isUnlocked = isUnlocked,
                        )
                    }
                }.toImmutableList()

            updateState { copy(badges = badgeDetails) }
        }
}
