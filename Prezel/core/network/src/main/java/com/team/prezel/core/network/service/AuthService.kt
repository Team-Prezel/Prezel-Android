package com.team.prezel.core.network.service

import com.team.prezel.core.network.auth.AuthRequestAttributes
import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginRequest
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.WithdrawRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Tag

internal interface AuthService {
    @POST("auth/logout")
    suspend fun logout(): ApiResponse<String>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
        @Tag(AuthRequestAttributes.SKIP_AUTH) skipAuth: Boolean = true,
    ): ApiResponse<LoginResponse>

    @DELETE("auth/withdraw")
    suspend fun withdraw(
        @Body request: WithdrawRequest,
    ): ApiResponse<String>
}
