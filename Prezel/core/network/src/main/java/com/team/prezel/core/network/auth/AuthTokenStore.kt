package com.team.prezel.core.network.auth

interface AuthTokenStore {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clear()
}
