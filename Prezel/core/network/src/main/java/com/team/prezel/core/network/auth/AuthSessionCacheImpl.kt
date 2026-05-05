package com.team.prezel.core.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.clearAuthTokens
import javax.inject.Inject
import javax.inject.Provider

internal class AuthSessionCacheImpl @Inject constructor(
    private val httpClientProvider: Provider<HttpClient>,
) : AuthSessionCache {
    override suspend fun clear() {
        httpClientProvider.get().clearAuthTokens()
    }
}
