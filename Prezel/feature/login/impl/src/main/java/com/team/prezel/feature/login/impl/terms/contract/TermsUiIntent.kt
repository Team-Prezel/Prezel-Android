package com.team.prezel.feature.login.impl.terms.contract

internal sealed interface TermsUiIntent {
    data object ToggleAll : TermsUiIntent

    data object ToggleTermsOfService : TermsUiIntent

    data object TogglePrivacyPolicy : TermsUiIntent

    data object ToggleMarketingConsent : TermsUiIntent

    data object ClickContinue : TermsUiIntent
}
