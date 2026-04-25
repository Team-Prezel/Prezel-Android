package com.team.prezel.core.network.auth

import com.team.prezel.core.model.auth.AuthToken

sealed interface AuthTokenRefreshResult {
    data class Success(
        val token: AuthToken,
    ) : AuthTokenRefreshResult

    sealed interface Failure : AuthTokenRefreshResult {
        val throwable: Throwable

        data class Retryable(
            override val throwable: Throwable,
        ) : Failure

        data class Unrecoverable(
            override val throwable: Throwable,
        ) : Failure
    }
}
