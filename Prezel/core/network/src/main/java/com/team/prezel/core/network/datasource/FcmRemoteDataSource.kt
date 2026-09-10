package com.team.prezel.core.network.datasource

interface FcmRemoteDataSource {
    suspend fun saveToken(token: String)

    suspend fun removeToken(token: String)
}
