package com.team.prezel.core.data.repository

import com.team.prezel.core.domain.AuthRepository
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.model.ApiResponse
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun login(
        provider: String,
        idToken: String,
    ): Result<Unit> =
        when (val response = authRemoteDataSource.login(idToken = idToken)) {
            is ApiResponse.Success -> Result.success(Unit)
            is ApiResponse.Failure.HttpError -> Result.failure(response.throwable)
            is ApiResponse.Failure.NetworkError -> Result.failure(response.throwable)
        }
}
