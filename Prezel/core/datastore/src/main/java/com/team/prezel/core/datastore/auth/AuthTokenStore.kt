package com.team.prezel.core.datastore.auth

/**
 * 인증 토큰의 메모리 캐시와 영속 저장소를 함께 관리하는 저장소 계약입니다.
 *
 * ### 초기화 계약
 * - 구현체는 생성 직후 영속 저장소의 값을 메모리 캐시에 적재할 수 있습니다.
 * - 호출부는 동기 getter의 초기 상태가 필요할 때 먼저 [awaitInitialized]를 호출해야 합니다.
 * - [awaitInitialized]가 정상 반환된 이후에는 동기 getter가 영속 저장소와 동기화된 최신 캐시 값을 반환해야 합니다.
 *
 * ### 동기 조회 계약
 * - [getAccessToken], [getRefreshToken]은 메모리 캐시의 현재 값을 즉시 반환해야 합니다.
 * - 호출 시점에 디스크 I/O나 네트워크 I/O를 유발하지 않아야 합니다.
 * - 따라서 OkHttp `Authenticator`와 같이 네트워크 스레드에서 호출되는 환경에서도 블로킹 없이 동작해야 합니다.
 *
 * ### 갱신 계약
 * - [saveTokens], [clear]는 영속 저장소 반영과 메모리 캐시 갱신을 함께 수행합니다.
 * - 두 함수가 정상적으로 반환된 직후에는 동기 getter가 항상 최신 값을 반환해야 합니다.
 */
interface AuthTokenStore {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    /**
     * 영속 저장소의 초기값을 메모리 캐시에 반영할 때까지 대기합니다.
     */
    suspend fun awaitInitialized()

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clear()
}
