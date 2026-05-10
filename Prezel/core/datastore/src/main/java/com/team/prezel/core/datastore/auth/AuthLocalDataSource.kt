package com.team.prezel.core.datastore.auth

import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    data class AuthTokens(
        val accessToken: String,
        val refreshToken: String,
    )

    val tokens: Flow<AuthTokens?>

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clearTokens()
}
