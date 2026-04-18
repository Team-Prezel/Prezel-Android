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
import kotlinx.coroutines.CancellationException
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

            try {
                val response =
                    refreshHttpClient
                        .post("${BuildConfig.BASE_URL}auth/reissue") {
                            contentType(ContentType.Application.Json)
                            setBody(ReissueTokenRequest(refreshToken = refreshToken))
                        }.body<LoginResponse>()

                authTokenStore.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                )
                if (BuildConfig.DEBUG) {
                    Timber.tag("AuthToken").d("토큰 재발급에 성공했습니다.")
                }
                response.accessToken
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                if (exception.isInvalidRefreshToken()) {
                    authTokenStore.clear()
                }
                Timber.e(exception, "토큰 재발급에 실패했습니다.")
                null
            }
        }

    private fun Throwable.isInvalidRefreshToken(): Boolean = this is ResponseException && response.status == HttpStatusCode.Unauthorized
}
