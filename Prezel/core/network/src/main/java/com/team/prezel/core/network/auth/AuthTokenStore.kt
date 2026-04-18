package com.team.prezel.core.network.auth

/**
 * 인증 토큰의 현재 값을 동기 조회하고, 영속 저장소와의 동기화를 비동기로 처리하는 저장소 계약입니다.
 *
 * ### 동기 조회 계약
 * - [getAccessToken], [getRefreshToken]은 메모리 캐시의 현재 값을 즉시 반환해야 합니다.
 * - 호출 시점에 디스크 I/O나 네트워크 I/O를 유발하지 않아야 합니다.
 * - 따라서 OkHttp `Authenticator`와 같이 네트워크 스레드에서 호출되는 환경에서도 블로킹 없이 동작해야 합니다.
 *
 * ### 갱신 계약
 * - [saveTokens], [clear]는 영속 저장소 반영과 메모리 캐시 갱신을 함께 수행합니다.
 * - 두 함수가 정상적으로 반환된 직후에는 동기 getter가 항상 최신 값을 반환해야 합니다.
 *
 */
interface AuthTokenStore {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clear()
}
