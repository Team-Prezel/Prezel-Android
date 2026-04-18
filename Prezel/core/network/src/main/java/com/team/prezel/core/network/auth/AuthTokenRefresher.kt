package com.team.prezel.core.network.auth

import com.team.prezel.core.datastore.auth.AuthTokenStore
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.model.ApiErrorResponse
import com.team.prezel.core.network.model.ApiResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenRefresher @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authTokenStore: AuthTokenStore,
) {
    private val mutex = Mutex()

    suspend fun refreshAccessToken(): String? =
        mutex.withLock {
            val refreshToken = authTokenStore.getRefreshToken() ?: return@withLock null

            when (val response = authRemoteDataSource.reissueToken(refreshToken = refreshToken)) {
                is ApiResponse.Success -> {
                    authTokenStore.saveTokens(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken,
                    )
                    if (BuildConfig.DEBUG) {
                        Timber.tag("AuthToken").d("토큰 재발급에 성공했습니다.")
                    }
                    response.data.accessToken
                }

                is ApiResponse.Failure.HttpError -> {
                    if (response.error.isSessionRecoveryUnrecoverable()) {
                        authTokenStore.clear()
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
