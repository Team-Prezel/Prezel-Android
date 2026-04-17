package com.team.prezel.core.network.auth

interface AuthTokenStore {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    fun clear()
}
