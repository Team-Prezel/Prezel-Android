package com.team.prezel.feature.login.impl.terms.contract

internal sealed interface TermsUiEffect {
    data object NavigateToHome : TermsUiEffect
}
