package com.team.prezel.core.network.auth

internal object AuthPathPolicy {
    private const val LOGIN_PATH = "/auth/login"
    private const val REISSUE_PATH = "/auth/reissue"

    fun requiresAuthorization(encodedPath: String): Boolean =
        !encodedPath.endsWith(LOGIN_PATH) && !encodedPath.endsWith(REISSUE_PATH)
}
