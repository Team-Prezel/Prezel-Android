package com.team.prezel.core.network.auth

import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.datasource.AuthLocalDataSource
import com.team.prezel.core.network.di.RefreshNetwork
import com.team.prezel.core.network.model.ApiErrorResponse
import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.ReissueTokenRequest
import com.team.prezel.core.network.service.AuthService
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthTokenRefresher @Inject constructor(
    @param:RefreshNetwork private val authService: AuthService,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    private val mutex = Mutex()

    suspend fun refreshTokens(): BearerTokens? =
        mutex.withLock {
            val refreshToken = authLocalDataSource.getToken()?.refreshToken ?: return@withLock null

            when (
                val response =
                    authService.reissueToken(
                        request = ReissueTokenRequest(refreshToken = refreshToken),
                    )
            ) {
                is ApiResponse.Success -> {
                    val token = AuthToken(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken,
                    )
                    authLocalDataSource.saveToken(token)
                    if (BuildConfig.DEBUG) {
                        Timber.tag("AuthToken").d("토큰 재발급에 성공했습니다.")
                    }
                    BearerTokens(
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken,
                    )
                }

                is ApiResponse.Failure.HttpError -> {
                    if (response.error.isSessionRecoveryUnrecoverable()) {
                        authLocalDataSource.clear()
                    }
                    Timber.e(response.throwable, "토큰 재발급에 실패했습니다.")
                    null
                }

                is ApiResponse.Failure.NetworkError -> {
                    Timber.e(response.throwable, "토큰 재발급에 실패했습니다.")
                    null
                }
            }
        }

    private fun ApiErrorResponse?.isSessionRecoveryUnrecoverable(): Boolean = this?.code == TOKEN_INVALID_CODE || this?.code == USER_NOT_FOUND_CODE

    private companion object {
        const val TOKEN_INVALID_CODE = "T001"
        const val USER_NOT_FOUND_CODE = "U003"
    }
}
