package com.team.prezel.core.datastore.auth

import com.team.prezel.core.model.auth.AuthToken

/**
 * 인증 토큰의 영속 저장소를 관리하는 저장소 계약입니다.
 *
 * Ktor BearerAuthProvider가 `loadTokens` 결과를 캐싱하므로, 이 저장소는 DataStore에
 * 저장된 값을 읽고 쓰는 역할만 담당합니다.
 */
interface AuthTokenStore {
    suspend fun getToken(): AuthToken?

    suspend fun saveToken(token: AuthToken)

    suspend fun clear()
}
