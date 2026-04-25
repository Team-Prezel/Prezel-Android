package com.team.prezel.core.network.datasource

import com.team.prezel.core.model.auth.AuthToken

interface AuthLocalDataSource {
    suspend fun getToken(): AuthToken?

    suspend fun saveToken(token: AuthToken)

    suspend fun clear()
}
