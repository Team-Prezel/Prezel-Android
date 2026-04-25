package com.team.prezel.feature.login.impl.terms.contract

import com.team.prezel.core.ui.base.UiEffect

internal sealed interface TermsUiEffect : UiEffect {
    data object NavigateToProfile : TermsUiEffect
}
