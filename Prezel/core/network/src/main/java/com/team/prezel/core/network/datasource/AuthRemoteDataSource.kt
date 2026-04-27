package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.auth.LoginResponse

interface AuthRemoteDataSource {
    suspend fun logout()

    suspend fun login(idToken: String): LoginResponse

    suspend fun withdraw(
        reasonCategory: String,
        reasonText: String,
    )
}
