package com.team.prezel.feature.login.impl.kakao

sealed interface KakaoLoginResult {
    data class Success(
        val accessToken: String,
    ) : KakaoLoginResult

    data class Failure(
        val throwable: Throwable,
    ) : KakaoLoginResult
}
