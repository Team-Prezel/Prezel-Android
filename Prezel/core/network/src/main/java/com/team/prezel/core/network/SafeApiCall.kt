package com.team.prezel.core.network

import com.team.prezel.core.network.model.ApiResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import java.io.IOException

suspend inline fun <reified T> safeApiCall(apiCall: () -> HttpResponse): ApiResponse<T> =
    try {
        val response = apiCall()
        ApiResponse.Success(response.body<T>())
    } catch (e: ClientRequestException) {
        ApiResponse.Error(e.response.status.value, e.message)
    } catch (e: ServerResponseException) {
        ApiResponse.Error(e.response.status.value, e.message)
    } catch (e: IOException) {
        ApiResponse.NetworkError
    }
