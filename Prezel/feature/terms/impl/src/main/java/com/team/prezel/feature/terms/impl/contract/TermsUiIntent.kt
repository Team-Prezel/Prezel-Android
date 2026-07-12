package com.team.prezel.feature.terms.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface TermsUiIntent : UiIntent {
    data object FetchTerms : TermsUiIntent

    data object ToggleAll : TermsUiIntent

    data class ToggleTerm(
        val termsId: Long,
    ) : TermsUiIntent

    data object ClickContinue : TermsUiIntent
}
