package com.team.prezel.core.model.auth

sealed interface AuthCheckResult {
    data object Loading : AuthCheckResult

    data object Authenticated : AuthCheckResult

    data object NeedsTermsAgreement : AuthCheckResult

    data object NeedsProfileCompletion : AuthCheckResult

    data object Unauthenticated : AuthCheckResult

    data object RetryableFailure : AuthCheckResult
}
