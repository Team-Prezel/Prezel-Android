package com.team.prezel.core.model.base

class ApiException(
    val status: Int,
    val errorCode: ServerErrorCode,
    override val message: String,
) : Exception(message)
