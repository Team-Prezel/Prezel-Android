package com.team.prezel.core.network

import com.team.prezel.core.network.model.ApiResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
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
                    is KtorfitResult.Success -> {
                        try {
                            val body =
                                result.response.body<Any>(bodyTypeInfo)
                            ApiResponse.Success(body)
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Timber.e(e, "Response parsing failed: ${e.message}")
                            ApiResponse.Failure.NetworkError(e)
                        }
                    }

                    is KtorfitResult.Failure -> {
                        when (val t = result.throwable) {
                            is CancellationException -> {
                                throw t
                            }

                            is IOException -> {
                                Timber.e(t, "Network error: ${t.message}")
                                ApiResponse.Failure.NetworkError(t)
                            }

                            is ResponseException -> {
                                Timber.e(t, "HTTP error ${t.response.status.value}: ${t.message}")
                                ApiResponse.Failure.HttpError(t)
                            }

                            else -> {
                                Timber.e(t, "Unknown error: ${t.message}")
                                ApiResponse.Failure.NetworkError(t)
                            }
                        }
                    }
                }
        }
    }
}
