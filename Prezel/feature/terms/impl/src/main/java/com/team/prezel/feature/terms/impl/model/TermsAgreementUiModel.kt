package com.team.prezel.feature.terms.impl.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class TermsAgreementUiModel(
    val termsId: Long,
    val title: String,
    val summary: String,
    val contentUrl: String,
    val isRequired: Boolean,
    val isChecked: Boolean = false,
)
