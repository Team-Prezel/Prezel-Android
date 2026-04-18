package com.team.prezel.core.domain.error

class ApiHttpException(
    val status: Int?,
    val code: String?,
    override val message: String?,
    cause: Throwable,
) : RuntimeException(message ?: cause.message, cause)
