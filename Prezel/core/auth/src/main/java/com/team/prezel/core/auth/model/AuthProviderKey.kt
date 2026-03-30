package com.team.prezel.core.auth.model

import dagger.MapKey

@MapKey
internal annotation class AuthProviderKey(
    val value: AuthProvider,
)
