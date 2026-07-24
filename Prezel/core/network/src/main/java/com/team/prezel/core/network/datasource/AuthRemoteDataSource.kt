package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.auth.LoginResponse
import com.team.prezel.core.network.model.auth.reissue.ReissueResponse

interface AuthRemoteDataSource {
    suspend fun logout()

    suspend fun login(idToken: String): LoginResponse

    suspend fun loginAdmin(): LoginResponse

    suspend fun reissue(refreshToken: String): ReissueResponse

    suspend fun withdraw(
        reasonCategory: String,
        reasonText: String?,
    )
}
