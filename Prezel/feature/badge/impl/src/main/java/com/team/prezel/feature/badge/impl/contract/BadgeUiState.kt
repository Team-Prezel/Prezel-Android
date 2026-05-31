package com.team.prezel.feature.badge.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.badge.impl.model.BadgeUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class BadgeUiState(
    val isLoading: Boolean = false,
    val badges: ImmutableList<BadgeUiModel> = persistentListOf(),
    val selectedBadgeCode: String? = null,
) : UiState {
    val selectedBadge: BadgeUiModel = badges.first { badge -> badge.badgeCode == selectedBadgeCode }
}
