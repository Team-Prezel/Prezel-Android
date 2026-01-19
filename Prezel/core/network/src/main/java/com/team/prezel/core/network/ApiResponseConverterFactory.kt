package com.team.prezel.core.network

import com.team.prezel.core.network.model.ApiResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import java.io.IOException

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
                        val response = result.response
                        try {
                            if (response.status.isSuccess()) {
                                val body = response.body<Any>(typeData.typeArgs.first().typeInfo)
                                ApiResponse.Success(body)
                            } else {
                                ApiResponse.Error(
                                    code = response.status.value,
                                    message = response.status.description,
                                )
                            }
                        } catch (e: Exception) {
                            ApiResponse.Error(
                                code = response.status.value,
                                message = e.message ?: "Unknown error",
                            )
                        }
                    }

                    is KtorfitResult.Failure -> {
                        when (result.throwable) {
                            is IOException -> ApiResponse.NetworkError

                            else -> ApiResponse.Error(
                                code = -1,
                                message = result.throwable.message ?: "Unknown error",
                            )
                        }
                    }
                }
        }
    }
}
