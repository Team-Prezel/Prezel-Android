package com.team.prezel.core.data.auth

sealed interface KakaoLoginResult {
    data class Success(
        val accessToken: String,
    ) : KakaoLoginResult

    data class Failure(
        val throwable: Throwable,
    ) : KakaoLoginResult
}
