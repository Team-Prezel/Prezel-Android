package com.team.prezel.core.network.model

import com.team.prezel.core.model.base.ApiException
import com.team.prezel.core.model.base.ServerErrorCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("code")
    val code: String,
    @SerialName("data")
    val data: T,
    @SerialName("message")
    val message: String,
)

internal fun <T> BaseResponse<T>.requireData(): T {
    if (status !in 200..299) {
        throw ApiException(
            status = status,
            errorCode = ServerErrorCode.from(code),
            message = message,
        )
    }

    return data
}
