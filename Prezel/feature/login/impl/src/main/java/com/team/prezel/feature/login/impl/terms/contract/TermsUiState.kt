package com.team.prezel.feature.login.impl.terms.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState

@Immutable
internal data class TermsUiState(
    val isTermsOfServiceChecked: Boolean = false,
    val isPrivacyPolicyChecked: Boolean = false,
    val isMarketingConsentChecked: Boolean = false,
) : UiState {
    val isRequiredChecked: Boolean = isTermsOfServiceChecked && isPrivacyPolicyChecked

    val isAllChecked: Boolean = isTermsOfServiceChecked && isPrivacyPolicyChecked && isMarketingConsentChecked
}
