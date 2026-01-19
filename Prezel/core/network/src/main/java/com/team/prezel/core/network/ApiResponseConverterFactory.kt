package com.team.prezel.core.network

import com.team.prezel.core.network.model.ApiResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ApiResponseConverterFactory : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit,
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type != ApiResponse::class) return null

        return object : Converter.SuspendResponseConverter<HttpResponse, ApiResponse<Any>> {
            override suspend fun convert(result: KtorfitResult): ApiResponse<Any> =
                when (result) {
                    is KtorfitResult.Success -> {
                        try {
                            val body =
                                result.response.body<Any>(typeData.typeArgs.first().typeInfo)
                            ApiResponse.Success(body)
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            ApiResponse.Failure.NetworkError
                        }
                    }

                    is KtorfitResult.Failure -> {
                        when (val t = result.throwable) {
                            is CancellationException -> throw t
                            is IOException -> ApiResponse.Failure.NetworkError
                            is ResponseException -> ApiResponse.Failure.HttpError(t)
                            else -> ApiResponse.Failure.NetworkError
                        }
                    }
                }
        }
    }
}
