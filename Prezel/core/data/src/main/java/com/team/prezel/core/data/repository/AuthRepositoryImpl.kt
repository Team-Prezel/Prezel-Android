package com.team.prezel.core.data.repository

import com.team.prezel.core.data.datasource.AuthLocalDataSource
import com.team.prezel.core.data.toResult
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.result.auth.AuthActionResult
import com.team.prezel.core.domain.result.auth.LoginStatusResult
import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginResponse
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {
    override suspend fun checkLoginStatus(): LoginStatusResult {
        authLocalDataSource.awaitInitialized()

        val accessToken = authLocalDataSource.getAccessToken()
        if (!accessToken.isNullOrBlank()) return LoginStatusResult.Authenticated

        val refreshToken = authLocalDataSource.getRefreshToken()
        if (refreshToken.isNullOrBlank()) return LoginStatusResult.Unauthenticated

        return when (val response = authRemoteDataSource.reissueToken(refreshToken = refreshToken)) {
            is ApiResponse.Success -> {
                saveTokens(response.data)
                LoginStatusResult.Authenticated
            }

            is ApiResponse.Failure.HttpError -> {
                if (response.error?.code == AUTHENTICATION_REQUIRED_CODE) {
                    authLocalDataSource.clear()
                    LoginStatusResult.Unauthenticated
                } else {
                    LoginStatusResult.RetryableFailure(response.throwable)
                }
            }

            is ApiResponse.Failure.NetworkError -> LoginStatusResult.RetryableFailure(response.throwable)
        }
    }

    override suspend fun logout(): AuthActionResult {
        if (authLocalDataSource.getAccessToken().isNullOrBlank()) return clearTokensAndAuthenticationRequired()

        return when (val response = authRemoteDataSource.logout()) {
            is ApiResponse.Success -> {
                authLocalDataSource.clear()
                AuthActionResult.Success
            }

            is ApiResponse.Failure.HttpError -> response.toAuthActionResult()
            is ApiResponse.Failure.NetworkError -> AuthActionResult.Failure(response.throwable)
        }
    }

    override suspend fun login(idToken: String): Result<Unit> =
        authRemoteDataSource
            .login(idToken = idToken)
            .toResult { response ->
                saveTokens(response)
                Unit
            }

    override suspend fun withdraw(reason: WithdrawReason): AuthActionResult {
        if (authLocalDataSource.getAccessToken().isNullOrBlank()) return clearTokensAndAuthenticationRequired()

        return when (
            val response =
                authRemoteDataSource.withdraw(
                    reasonCategory = reason.toCategory(),
                    reasonText = reason.toReasonText(),
                )
        ) {
            is ApiResponse.Success -> {
                authLocalDataSource.clear()
                AuthActionResult.Success
            }

            is ApiResponse.Failure.HttpError -> response.toAuthActionResult()
            is ApiResponse.Failure.NetworkError -> AuthActionResult.Failure(response.throwable)
        }
    }

    private suspend fun saveTokens(response: LoginResponse): AuthToken =
        response
            .toAuthToken()
            .also { token ->
                authLocalDataSource.saveTokens(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                )
            }

    private fun LoginResponse.toAuthToken(): AuthToken =
        AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private suspend fun ApiResponse.Failure.HttpError.toAuthActionResult(): AuthActionResult =
        if (error?.code == AUTHENTICATION_REQUIRED_CODE) {
            authLocalDataSource.clear()
            AuthActionResult.AuthenticationRequired
        } else {
            AuthActionResult.Failure(throwable)
        }

    private suspend fun clearTokensAndAuthenticationRequired(): AuthActionResult {
        authLocalDataSource.clear()
        return AuthActionResult.AuthenticationRequired
    }

    private fun WithdrawReason.toCategory(): String =
        when (this) {
            WithdrawReason.NotUsedOften -> "NOT_USED_OFTEN"
            WithdrawReason.NoLongerNeeded -> "NO_LONGER_NEEDED"
            WithdrawReason.TooDifficultOrComplex -> "TOO_COMPLEX"
            WithdrawReason.AnalysisResultInaccurate -> "INACCURATE_ANALYSIS"
            WithdrawReason.TooManyErrors -> "MANY_ERRORS"
            is WithdrawReason.Other -> "ETC"
        }

    private fun WithdrawReason.toReasonText(): String =
        when (this) {
            is WithdrawReason.Other -> text
            else -> ""
        }

    private companion object {
        const val AUTHENTICATION_REQUIRED_CODE = "U001"
    }
}
