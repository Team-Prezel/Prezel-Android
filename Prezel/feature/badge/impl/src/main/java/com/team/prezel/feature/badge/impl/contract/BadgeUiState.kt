package com.team.prezel.feature.badge.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel

@Immutable
internal data class BadgeUiState(
    val isLoading: Boolean = false,
    val badgeDetail: BadgeDetailUiModel? = null,
) : UiState
