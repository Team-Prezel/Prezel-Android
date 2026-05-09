package com.team.prezel.core.model.auth

sealed interface WithdrawReason {
    data object NotUsedOften : WithdrawReason

    data object NoLongerNeeded : WithdrawReason

    data object TooComplex : WithdrawReason

    data object InaccurateAnalysis : WithdrawReason

    data object ManyErrors : WithdrawReason

    data class Etc(
        val text: String,
    ) : WithdrawReason
}
