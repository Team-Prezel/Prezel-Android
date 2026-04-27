package com.team.prezel.core.datastore.auth

import com.team.prezel.core.model.auth.AuthTokens
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    val tokens: Flow<AuthTokens?>

    suspend fun saveTokens(accessToken: String, refreshToken: String)

    suspend fun clearTokens()
}
