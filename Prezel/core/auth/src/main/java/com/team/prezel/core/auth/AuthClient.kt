package com.team.prezel.core.auth

import android.content.Context
import com.team.prezel.core.auth.model.AuthResult

interface AuthClient {
    suspend fun login(context: Context): AuthResult

    suspend fun logout(): Result<Unit>
}
