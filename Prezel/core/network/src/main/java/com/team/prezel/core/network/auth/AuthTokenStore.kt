package com.team.prezel.core.network.auth

import com.team.prezel.core.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthTokenStore {
    fun getToken(): Flow<AuthToken?>

    suspend fun saveToken(token: AuthToken): Result<Unit>

    suspend fun clear(): Result<Unit>
}
