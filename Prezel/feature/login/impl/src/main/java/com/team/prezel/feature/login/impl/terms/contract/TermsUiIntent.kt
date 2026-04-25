package com.team.prezel.feature.login.impl.terms.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface TermsUiIntent : UiIntent {
    data object ToggleAll : TermsUiIntent

    data object ToggleTermsOfService : TermsUiIntent

    data object TogglePrivacyPolicy : TermsUiIntent

    data object ToggleMarketingConsent : TermsUiIntent

    data object ClickContinue : TermsUiIntent
}
