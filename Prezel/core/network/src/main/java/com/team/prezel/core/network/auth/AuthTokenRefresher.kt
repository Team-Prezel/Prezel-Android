package com.team.prezel.core.network.auth

import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.datasource.AuthLocalDataSource
import com.team.prezel.core.network.model.ApiErrorResponse
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.ReissueTokenRequest
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class AuthTokenRefresher @Inject constructor(
    private val json: Json,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    private val mutex = Mutex()

    suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens? =
        mutex.withLock {
            val refreshToken = params.oldTokens?.refreshToken
                ?: authLocalDataSource.getToken()?.refreshToken
                ?: return@withLock null

            try {
                val response = params.client
                    .post("${BuildConfig.BASE_URL}auth/reissue") {
                        with(params) {
                            markAsRefreshTokenRequest()
                        }
                        setBody(ReissueTokenRequest(refreshToken = refreshToken))
                    }.body<LoginResponse>()

                val token = AuthToken(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                )
                authLocalDataSource.saveToken(token)
                if (BuildConfig.DEBUG) {
                    Timber.tag("AuthToken").d("토큰 재발급에 성공했습니다.")
                }
                BearerTokens(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                )
            } catch (t: Throwable) {
                t.rethrowIfCancellation()
                if (t.isSessionRecoveryUnrecoverable()) {
                    authLocalDataSource.clear()
                }
                Timber.e(t, "토큰 재발급에 실패했습니다.")
                null
            }
        }

    private suspend fun Throwable.isSessionRecoveryUnrecoverable(): Boolean {
        if (this !is ResponseException) return false

        val error = parseErrorResponse()
        return error?.code == TOKEN_INVALID_CODE || error?.code == USER_NOT_FOUND_CODE
    }

    private suspend fun ResponseException.parseErrorResponse(): ApiErrorResponse? =
        try {
            json.decodeFromString<ApiErrorResponse>(response.bodyAsText())
        } catch (t: Throwable) {
            t.rethrowIfCancellation()
            null
        }

    private fun Throwable.rethrowIfCancellation() {
        if (this is CancellationException) throw this
    }

    private companion object {
        const val TOKEN_INVALID_CODE = "T001"
        const val USER_NOT_FOUND_CODE = "U003"
    }
}
