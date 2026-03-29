package com.team.prezel.core.auth

sealed interface KakaoLoginResult {
    data object Success : KakaoLoginResult

    data class RateLimited(
        val throwable: Throwable,
    ) : KakaoLoginResult

    data class Failure(
        val throwable: Throwable,
    ) : KakaoLoginResult
}
