package com.team.prezel.core.datastore.auth

import com.team.prezel.core.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow

/**
 * 인증 토큰을 로컬 저장소에서 읽고 쓰는 데이터 소스 계약입니다.
 */
interface AuthLocalDataSource {
    fun getToken(): Flow<AuthToken?>

    suspend fun saveToken(
        token: AuthToken,
        invalidateCache: Boolean = true,
    ): Result<Unit>

    suspend fun clear(): Result<Unit>
}
