package com.team.prezel.core.network.auth

interface AuthSessionCache {
    suspend fun clear()
}
