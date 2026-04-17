package com.team.prezel.core.network.auth

import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.ReissueTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthTokenRefresher @Inject constructor(
    @param:Named("refresh")
    private val refreshHttpClient: HttpClient,
    private val authTokenStore: AuthTokenStore,
) {
    private val mutex = Mutex()

    suspend fun refreshAccessToken(): String? =
        mutex.withLock {
            val refreshToken = authTokenStore.getRefreshToken() ?: return@withLock null

            runCatching {
                refreshHttpClient
                    .post("${BuildConfig.BASE_URL}auth/reissue") {
                        contentType(ContentType.Application.Json)
                        setBody(ReissueTokenRequest(refreshToken = refreshToken))
                    }.body<LoginResponse>()
            }.onSuccess { response ->
                authTokenStore.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                )
                if (BuildConfig.DEBUG) {
                    Timber.tag("AuthToken").d("Refreshed accessToken=%s", response.accessToken)
                    Timber.tag("AuthToken").d("Refreshed refreshToken=%s", response.refreshToken)
                }
            }.onFailure { throwable ->
                if (throwable.isInvalidRefreshToken()) {
                    authTokenStore.clear()
                }
                Timber.e(throwable, "토큰 재발급에 실패했습니다.")
            }.getOrNull()
                ?.accessToken
        }

    private fun Throwable.isInvalidRefreshToken(): Boolean = this is ResponseException && response.status == HttpStatusCode.Unauthorized
}
