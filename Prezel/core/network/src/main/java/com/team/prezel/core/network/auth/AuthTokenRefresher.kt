package com.team.prezel.core.network.auth

import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.model.ApiErrorResponse
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.ReissueTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class AuthTokenRefresher @Inject constructor(
    private val json: Json,
    private val authTokenStore: AuthTokenStore,
    private val authSessionExpiredNotifier: AuthSessionExpiredNotifier,
) {
    private val mutex = Mutex()

    suspend fun refreshBearerTokens(params: RefreshTokensParams): BearerTokens? =
        mutex.withLock {
            val storedToken = authTokenStore.getToken().first()
            val oldRefreshToken = params.oldTokens?.refreshToken

            if (
                oldRefreshToken != null &&
                storedToken != null &&
                storedToken.refreshToken != oldRefreshToken
            ) {
                return@withLock storedToken.toBearerTokens()
            }

            val refreshToken = oldRefreshToken
                ?: storedToken?.refreshToken
                ?: return@withLock null

            when (
                val result =
                    requestTokenReissue(
                        client = params.client,
                        refreshToken = refreshToken,
                        markAsRefreshTokenRequest = {
                            with(params) {
                                markAsRefreshTokenRequest()
                            }
                        },
                    )
            ) {
                is AuthTokenRefreshResult.Success ->
                    BearerTokens(
                        accessToken = result.token.accessToken,
                        refreshToken = result.token.refreshToken,
                    )

                is AuthTokenRefreshResult.Failure -> null
            }
        }

    private fun AuthToken.toBearerTokens(): BearerTokens =
        BearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private suspend fun requestTokenReissue(
        client: HttpClient,
        refreshToken: String,
        markAsRefreshTokenRequest: HttpRequestBuilder.() -> Unit,
    ): AuthTokenRefreshResult =
        try {
            val response = client
                .post("${BuildConfig.BASE_URL}auth/reissue") {
                    markAsRefreshTokenRequest()
                    attributes.put(AuthRequestAttributes.SkipAuthKey, true)
                    setBody(ReissueTokenRequest(refreshToken = refreshToken))
                }.body<LoginResponse>()

            val token = AuthToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
            authTokenStore
                .saveToken(token)
                .onFailure { throwable ->
                    Timber.e(throwable, "재발급된 토큰 저장에 실패했습니다.")
                    return AuthTokenRefreshResult.Failure.Retryable(throwable)
                }
            if (BuildConfig.DEBUG) {
                Timber.tag("AuthToken").d("토큰 재발급에 성공했습니다.")
            }
            AuthTokenRefreshResult.Success(token)
        } catch (t: Throwable) {
            t.rethrowIfCancellation()
            if (t.isSessionRecoveryUnrecoverable()) {
                authTokenStore.clear().fold(
                    onSuccess = {
                        authSessionExpiredNotifier.notifySessionExpired()
                    },
                    onFailure = { throwable ->
                        Timber.e(throwable, "인증 토큰 삭제에 실패했습니다.")
                    },
                )
                Timber.e(t, "토큰 재발급에 실패했습니다.")
                AuthTokenRefreshResult.Failure.Unrecoverable(t)
            } else {
                Timber.e(t, "토큰 재발급에 실패했습니다.")
                AuthTokenRefreshResult.Failure.Retryable(t)
            }
        }

    private suspend fun Throwable.isSessionRecoveryUnrecoverable(): Boolean {
        if (this !is ResponseException) return false

        val error = try {
            json.decodeFromString<ApiErrorResponse>(response.bodyAsText())
        } catch (t: Throwable) {
            t.rethrowIfCancellation()
            null
        }

        return error?.code == TOKEN_INVALID_CODE ||
            error?.code == AUTHENTICATION_REQUIRED_CODE ||
            error?.code == USER_NOT_FOUND_CODE
    }

    fun Throwable.rethrowIfCancellation() {
        if (this is CancellationException) throw this
    }

    private companion object {
        const val TOKEN_INVALID_CODE = "T001"
        const val AUTHENTICATION_REQUIRED_CODE = "U001"
        const val USER_NOT_FOUND_CODE = "U003"
    }
}
