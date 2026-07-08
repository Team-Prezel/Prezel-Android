package com.team.prezel.feature.my.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.badge.FetchBadgesUseCase
import com.team.prezel.core.domain.usecase.user.FetchUserInfoUseCase
import com.team.prezel.core.model.badge.BadgeSortType
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.my.impl.contract.MyUiEffect
import com.team.prezel.feature.my.impl.contract.MyUiIntent
import com.team.prezel.feature.my.impl.contract.MyUiState
import com.team.prezel.feature.my.impl.model.BadgeUiModel
import com.team.prezel.feature.my.impl.model.MyUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class MyViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val fetchBadgesUseCase: FetchBadgesUseCase,
) : BaseViewModel<MyUiState, MyUiIntent, MyUiEffect>(MyUiState()) {
    override fun onIntent(intent: MyUiIntent) {
        when (intent) {
            is MyUiIntent.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            val userDeferred = async { fetchUserInfo() }
            val badgesDeferred = async { fetchMyBadges() }

            val user = userDeferred.await()
            val badges = badgesDeferred.await()

            updateState {
                copy(
                    isLoading = false,
                    profileImageUrl = user?.profileImageUrl,
                    nickname = user?.nickname ?: this.nickname,
                    badges = badges ?: this.badges,
                )
            }
        }
    }

    private suspend fun fetchUserInfo(): User? =
        fetchUserInfoUseCase().fold(
            onSuccess = { user -> user },
            onFailure = { throwable ->
                Timber.e(t = throwable)
                sendEffect(MyUiEffect.ShowMessage(MyUiMessage.FETCH_USER_INFO_FAILED))
                null
            },
        )

    private suspend fun fetchMyBadges(): ImmutableList<BadgeUiModel>? =
        fetchBadgesUseCase(sort = BadgeSortType.ACQUIRED).fold(
            onSuccess = { badges ->
                badges
                    .map { badge ->
                        BadgeUiModel(code = badge.badgeCode, title = badge.badgeName, imageUrl = badge.imageUrl, isAchieved = badge.isUnlocked)
                    }.toImmutableList()
            },
            onFailure = { throwable ->
                Timber.e(t = throwable)
                sendEffect(MyUiEffect.ShowMessage(MyUiMessage.FETCH_USER_BADGES_FAILED))
                null
            },
        )
}
