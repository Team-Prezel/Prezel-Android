package com.team.prezel.core.domain.error

class AuthenticationRequiredException(
    override val message: String = "인증이 필요합니다.",
) : IllegalStateException(message)
