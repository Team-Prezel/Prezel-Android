package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginResponse

interface AuthRemoteDataSource {
    suspend fun reissueToken(refreshToken: String): ApiResponse<LoginResponse>

    suspend fun logout(accessToken: String): ApiResponse<String>

    suspend fun login(idToken: String): ApiResponse<LoginResponse>

    suspend fun withdraw(
        accessToken: String,
        reasonCategory: String,
        reasonText: String,
    ): ApiResponse<String>
}
