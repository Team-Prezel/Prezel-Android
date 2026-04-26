package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginRequest
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.WithdrawRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.POST

internal interface AuthService {
    @POST("auth/logout")
    suspend fun logout(): ApiResponse<String>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): ApiResponse<LoginResponse>

    @DELETE("auth/withdraw")
    suspend fun withdraw(
        @Body request: WithdrawRequest,
    ): ApiResponse<String>
}
