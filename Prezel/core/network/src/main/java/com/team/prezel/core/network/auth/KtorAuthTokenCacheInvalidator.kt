package com.team.prezel.core.network.auth

import com.team.prezel.core.datastore.auth.AuthTokenCacheInvalidator
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.clearAuthTokens
import javax.inject.Inject
import javax.inject.Provider

internal class KtorAuthTokenCacheInvalidator @Inject constructor(
    private val httpClientProvider: Provider<HttpClient>,
) : AuthTokenCacheInvalidator {
    override fun invalidate() {
        httpClientProvider.get().clearAuthTokens()
    }
}
