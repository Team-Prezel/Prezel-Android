package com.team.prezel.core.model.auth

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)
