package com.team.prezel.core.network.auth

interface TokenProvider {
    suspend fun getTokens(): AuthTokens?

    suspend fun updateTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clearTokens()

    data class AuthTokens(
        val accessToken: String,
        val refreshToken: String,
    )
}
