package com.team.prezel.feature.terms.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.terms.impl.model.TermsUiMessage

internal sealed interface TermsUiEffect : UiEffect {
    data object NavigateToProfile : TermsUiEffect

    data class ShowMessage(
        val message: TermsUiMessage,
    ) : TermsUiEffect
}
