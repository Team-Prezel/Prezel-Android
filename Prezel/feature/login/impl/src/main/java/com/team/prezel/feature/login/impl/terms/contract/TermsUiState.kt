package com.team.prezel.feature.login.impl.terms.contract

import androidx.compose.runtime.Immutable

@Immutable
internal data class TermsUiState(
    val isTermsOfServiceChecked: Boolean = false,
    val isPrivacyPolicyChecked: Boolean = false,
    val isMarketingConsentChecked: Boolean = false,
) {
    val isRequiredChecked: Boolean = isTermsOfServiceChecked && isPrivacyPolicyChecked

    val isAllChecked: Boolean = isTermsOfServiceChecked && isPrivacyPolicyChecked && isMarketingConsentChecked
}
