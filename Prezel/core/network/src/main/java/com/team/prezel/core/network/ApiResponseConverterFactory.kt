package com.team.prezel.core.network

import com.team.prezel.core.network.model.ApiResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ApiResponseConverterFactory : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit,
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type != ApiResponse::class) return null
        val bodyTypeInfo = typeData.typeArgs.firstOrNull()?.typeInfo ?: return null

        return object : Converter.SuspendResponseConverter<HttpResponse, ApiResponse<Any>> {
            override suspend fun convert(result: KtorfitResult): ApiResponse<Any> =
                when (result) {
                    is KtorfitResult.Success -> parseSuccess(result.response, bodyTypeInfo)
                    is KtorfitResult.Failure -> mapFailure(result.throwable)
                }
        }
    }

    private suspend fun parseSuccess(
        response: HttpResponse,
        bodyTypeInfo: TypeInfo,
    ): ApiResponse<Any> =
        try {
            val body = response.body<Any>(bodyTypeInfo)
            ApiResponse.Success(body)
        } catch (t: Throwable) {
            t.rethrowIfCancellation()
            Timber.e(t, "Response parsing failed")
            ApiResponse.Failure.NetworkError(t)
        }

    private fun mapFailure(t: Throwable): ApiResponse<Any> {
        t.rethrowIfCancellation()

        return when (t) {
            is IOException -> {
                Timber.e(t, "Network error")
                ApiResponse.Failure.NetworkError(t)
            }

            is ResponseException -> {
                Timber.e(t, "HTTP error ${t.response.status.value}")
                ApiResponse.Failure.HttpError(t)
            }

            else -> {
                Timber.e(t, "Unknown error")
                ApiResponse.Failure.NetworkError(t)
            }
        }
    }

    private fun Throwable.rethrowIfCancellation() {
        if (this is CancellationException) throw this
    }
}
