package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.auth.LoginRequest
import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.WithdrawRequest
import com.team.prezel.core.network.model.auth.reissue.ReissueRequest
import com.team.prezel.core.network.model.auth.reissue.ReissueResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.service.AuthService
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService,
) : AuthRemoteDataSource {
    override suspend fun logout() {
        authService.logout().requireSuccess()
    }

    override suspend fun login(idToken: String): LoginResponse = authService.login(request = LoginRequest(idToken = idToken)).requireData()

    override suspend fun reissue(refreshToken: String): ReissueResponse =
        authService.reissue(request = ReissueRequest(refreshToken = refreshToken)).requireData()

    override suspend fun withdraw(
        reasonCategory: String,
        reasonText: String?,
    ) {
        authService
            .withdraw(request = WithdrawRequest(reasonCategory = reasonCategory, reasonText = reasonText))
            .requireSuccess()
    }
}
