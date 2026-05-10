package com.team.prezel.feature.my.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.my.impl.model.BadgeUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class MyUiState(
    val isLoading: Boolean = false,
    val profileImageUrl: String? = null,
    val nickname: String = "",
    val badges: ImmutableList<BadgeUiModel> = persistentListOf(),
) : UiState
