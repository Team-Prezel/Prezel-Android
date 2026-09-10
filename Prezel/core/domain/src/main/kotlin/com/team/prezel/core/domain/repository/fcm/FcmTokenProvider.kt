package com.team.prezel.core.domain.repository.fcm

interface FcmTokenProvider {
    suspend fun getToken(): Result<String>
}
