package com.team.prezel.feature.terms.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.terms.impl.model.TermsAgreementUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class TermsUiState(
    val terms: ImmutableList<TermsAgreementUiModel> = persistentListOf(),
    val isTermsLoading: Boolean = false,
    val isLoading: Boolean = false,
) : UiState {
    val isRequiredChecked: Boolean = terms.any { it.isRequired } && terms.filter { it.isRequired }.all { it.isChecked }

    val isAllChecked: Boolean = terms.isNotEmpty() && terms.all { it.isChecked }
}
