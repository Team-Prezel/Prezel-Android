package com.team.prezel.core.model.auth

sealed interface WithdrawReason {
    data object NotUsedOften : WithdrawReason

    data object NoLongerNeeded : WithdrawReason

    data object TooDifficultOrComplex : WithdrawReason

    data object AnalysisResultInaccurate : WithdrawReason

    data object TooManyErrors : WithdrawReason

    data class Other(
        val text: String,
    ) : WithdrawReason
}
