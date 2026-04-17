package com.team.prezel.core.network.auth

import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshAuthenticator @Inject constructor(
    private val authTokenRefresher: AuthTokenRefresher,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (!response.request.url.encodedPath
                .requiresAuthorization()
        ) {
            return null
        }
        if (responseCount(response) >= MAX_AUTH_RETRY_COUNT) return null

        val refreshedAccessToken =
            runBlocking { authTokenRefresher.refreshAccessToken() } ?: return null

        return response.request
            .newBuilder()
            .removeHeader(HttpHeaders.Authorization)
            .addHeader(HttpHeaders.Authorization, "Bearer $refreshedAccessToken")
            .build()
    }

    private fun String.requiresAuthorization(): Boolean = this != LOGIN_PATH && this != REISSUE_PATH

    private fun responseCount(response: Response): Int {
        var count = 1
        var current = response.priorResponse
        while (current != null) {
            count++
            current = current.priorResponse
        }
        return count
    }

    private companion object {
        const val LOGIN_PATH = "/auth/login"
        const val REISSUE_PATH = "/auth/reissue"
        const val MAX_AUTH_RETRY_COUNT = 2
    }
}
