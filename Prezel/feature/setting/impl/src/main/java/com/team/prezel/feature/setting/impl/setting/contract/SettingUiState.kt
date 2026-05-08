package com.team.prezel.feature.setting.impl.setting.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class SettingUiState(
    val isLoading: Boolean = false,
    val profileImageUrl: String? = null,
    val nickname: String = "",
    val email: String = "",
) : UiState
