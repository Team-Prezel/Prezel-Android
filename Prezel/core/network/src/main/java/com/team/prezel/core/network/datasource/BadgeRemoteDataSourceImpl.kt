package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.badge.BadgeEventResponse
import com.team.prezel.core.network.model.badge.GetBadgeDetailResponse
import com.team.prezel.core.network.model.badge.GetBadgeResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.service.BadgeService
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

internal class BadgeRemoteDataSourceImpl @Inject constructor(
    private val badgeService: BadgeService,
    private val httpClient: HttpClient,
) : BadgeRemoteDataSource {
    private val sseJson: Json =
        Json {
            ignoreUnknownKeys = true
        }

    override suspend fun getBadges(): List<GetBadgeResponse> = badgeService.getBadges().requireData()

    override suspend fun getBadgeDetail(badgeCode: String): GetBadgeDetailResponse = badgeService.getBadgeDetail(badgeCode = badgeCode).requireData()

    override fun connectBadgeEventStream(): Flow<BadgeEventResponse> =
        callbackFlow {
            val response = httpClient.get("api/stream/badges") {
                accept(ContentType.Text.EventStream)
            }

            val readerJob = launch {
                try {
                    response.readBadgeEvents { event ->
                        trySend(event)
                    }
                    close()
                } catch (throwable: CancellationException) {
                    throw throwable
                } catch (throwable: Throwable) {
                    close(throwable)
                }
            }

            awaitClose {
                readerJob.cancel()
            }
        }

    private suspend fun HttpResponse.readBadgeEvents(emit: suspend (BadgeEventResponse) -> Unit) {
        val channel = bodyAsChannel()
        var eventName: String? = null
        val dataBuffer = StringBuilder()

        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break
            Timber.tag("TEST").i("Event: $line")

            when {
                line.isBlank() -> {
                    dispatchBadgeEvent(
                        eventName = eventName,
                        data = dataBuffer.toString(),
                        emit = emit,
                    )
                    eventName = null
                    dataBuffer.clear()
                }

                line.startsWith("event:") -> {
                    eventName = line.substringAfter("event:").trim().takeIf(String::isNotBlank)
                }

                line.startsWith("data:") -> {
                    if (dataBuffer.isNotEmpty()) {
                        dataBuffer.append('\n')
                    }
                    dataBuffer.append(line.substringAfter("data:").trimStart())
                }
            }
        }

        dispatchBadgeEvent(
            eventName = eventName,
            data = dataBuffer.toString(),
            emit = emit,
        )
    }

    private suspend fun dispatchBadgeEvent(
        eventName: String?,
        data: String,
        emit: suspend (BadgeEventResponse) -> Unit,
    ) {
        val rawData = data.trim()
        if (rawData.isBlank()) return

        emit(rawData.toBadgeEventResponse(eventName = eventName))
    }

    private fun String.toBadgeEventResponse(eventName: String?): BadgeEventResponse {
        val payload = runCatching {
            sseJson.decodeFromString<BadgeEventPayload>(this)
        }.getOrNull()

        return BadgeEventResponse(
            badgeCode = payload?.badgeCode,
            badgeName = payload?.badgeName,
            message = payload?.message ?: eventName,
            rawData = this,
        )
    }

    @Serializable
    private data class BadgeEventPayload(
        val badgeCode: String? = null,
        val badgeName: String? = null,
        val message: String? = null,
    )
}
