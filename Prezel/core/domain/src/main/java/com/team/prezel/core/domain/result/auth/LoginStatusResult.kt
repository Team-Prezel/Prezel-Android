package com.team.prezel.core.domain.result.auth

sealed interface LoginStatusResult {
    data object Authenticated : LoginStatusResult

    data object Unauthenticated : LoginStatusResult

    data class RetryableFailure(
        val throwable: Throwable,
    ) : LoginStatusResult
}
