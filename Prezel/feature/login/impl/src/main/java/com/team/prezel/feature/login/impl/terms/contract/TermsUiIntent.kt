package com.team.prezel.feature.login.impl.terms.contract

internal sealed interface TermsUiIntent {
    data object OnToggleAll : TermsUiIntent

    data object OnToggleTermsOfService : TermsUiIntent

    data object OnTogglePrivacyPolicy : TermsUiIntent

    data object OnToggleMarketingConsent : TermsUiIntent

    data object OnClickContinue : TermsUiIntent
}
