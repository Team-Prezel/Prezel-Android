package com.team.prezel.feature.setting.impl.setting.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.terms.Term
import com.team.prezel.core.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class SettingUiState(
    val isLoading: Boolean = false,
    val profileImageUrl: String? = null,
    val nickname: String = "",
    val email: String = "",
    val terms: ImmutableList<Term> = persistentListOf(),
) : UiState
