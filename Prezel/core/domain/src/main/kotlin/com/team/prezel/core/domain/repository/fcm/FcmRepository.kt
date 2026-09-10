package com.team.prezel.core.domain.repository.fcm

interface FcmRepository {
    suspend fun saveToken(token: String): Result<Unit>

    suspend fun removeToken(token: String): Result<Unit>
}
