package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginRequest
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.WithdrawRequest
import com.team.prezel.core.network.service.AuthService
import timber.log.Timber
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService,
) : AuthRemoteDataSource {
    override suspend fun logout(): ApiResponse<String> = authService.logout()

    override suspend fun login(idToken: String): ApiResponse<LoginResponse> =
        authService
            .login(request = LoginRequest(idToken = idToken))
            .also(::logTokenResponse)

    override suspend fun withdraw(
        reasonCategory: String,
        reasonText: String,
    ): ApiResponse<String> =
        authService.withdraw(
            request = WithdrawRequest(
                reasonCategory = reasonCategory,
                reasonText = reasonText,
            ),
        )

    private fun logTokenResponse(response: ApiResponse<LoginResponse>) {
        if (!BuildConfig.DEBUG) return

        when (response) {
            is ApiResponse.Success -> Timber.tag("AuthToken").d("서버 인증 응답에 성공했습니다.")

            is ApiResponse.Failure.HttpError -> {
                Timber.tag("AuthToken").e(response.throwable, "서버 로그인에 실패했습니다: http error")
            }

            is ApiResponse.Failure.NetworkError -> {
                Timber.tag("AuthToken").e(response.throwable, "서버 로그인에 실패했습니다: network error")
            }
        }
    }
}
