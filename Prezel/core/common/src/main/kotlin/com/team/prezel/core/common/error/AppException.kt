package com.team.prezel.core.common.error

class AppException(
    val error: AppError,
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause)
