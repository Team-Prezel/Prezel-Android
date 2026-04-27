package com.team.prezel.core.network.service

import com.team.prezel.core.network.auth.AuthRequestAttributes
import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.auth.LoginRequest
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.WithdrawRequest
import com.team.prezel.core.network.model.auth.reissue.ReissueRequest
import com.team.prezel.core.network.model.auth.reissue.ReissueResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Tag

internal interface AuthService {
    @POST("auth/logout")
    suspend fun logout(): BaseResponse<String>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
        @Tag(AuthRequestAttributes.SKIP_AUTH) skipAuth: Boolean = true,
    ): BaseResponse<LoginResponse>

    @DELETE("auth/withdraw")
    suspend fun withdraw(
        @Body request: WithdrawRequest,
    ): BaseResponse<String>

    @POST("auth/reissue")
    suspend fun reissue(
        @Body request: ReissueRequest,
        @Tag(AuthRequestAttributes.SKIP_AUTH) skipAuth: Boolean = true,
    ): BaseResponse<ReissueResponse>
}
