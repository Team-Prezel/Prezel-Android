package com.team.prezel.core.data.auth

import android.content.Context

interface KakaoLoginManager {
    suspend fun login(context: Context): KakaoLoginResult

    suspend fun logout(): Result<Unit>
}
