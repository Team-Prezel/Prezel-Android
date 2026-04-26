package com.team.prezel.core.network.auth

import io.ktor.util.AttributeKey

internal object AuthRequestAttributes {
    const val SKIP_AUTH = "skipAuth"

    val SkipAuthKey = AttributeKey<Boolean>(SKIP_AUTH)
}
