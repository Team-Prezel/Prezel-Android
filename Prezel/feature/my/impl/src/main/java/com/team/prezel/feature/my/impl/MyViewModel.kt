package com.team.prezel.feature.my.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.user.FetchUserBadgesUseCase
import com.team.prezel.core.domain.usecase.user.FetchUserInfoUseCase
import com.team.prezel.core.model.badge.Badge
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.my.impl.contract.MyUiEffect
import com.team.prezel.feature.my.impl.contract.MyUiIntent
import com.team.prezel.feature.my.impl.contract.MyUiState
import com.team.prezel.feature.my.impl.model.BadgeUiModel
import com.team.prezel.feature.my.impl.model.MyUiMessage
import com.team.prezel.feature.my.impl.model.toUiModel
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
    private val fetchUserBadgesUseCase: FetchUserBadgesUseCase,
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
                    profileImageUrl = user?.profileImage?.url ?: this.profileImageUrl,
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
        fetchUserBadgesUseCase().fold(
            onSuccess = { badges -> badges.map(Badge::toUiModel).toImmutableList() },
            onFailure = { throwable ->
                Timber.e(t = throwable)
                sendEffect(MyUiEffect.ShowMessage(MyUiMessage.FETCH_USER_BADGES_FAILED))
                null
            },
        )
}
