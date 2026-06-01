package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.badge.BadgeEventResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber

internal object BadgeSseEventParser {
    private val sseJson: Json =
        Json {
            ignoreUnknownKeys = true
        }

    fun parse(
        eventName: String?,
        data: String?,
    ): BadgeEventResponse? {
        Timber.tag("SSE-TEST").i("$eventName: $data")
        val rawData = data?.trim().orEmpty()
        if (rawData.isBlank()) return null

        return when (eventName) {
            SSE_EVENT_BADGE_UNLOCKED -> {
                val payload = runCatching {
                    sseJson.decodeFromString<BadgeEventPayload>(rawData)
                }.getOrNull() ?: return null

                BadgeEventResponse(
                    badgeCode = payload.badgeCode,
                    badgeName = payload.badgeName,
                    introduction = payload.introduction,
                    imageUrl = payload.imageUrl,
                    message = payload.message,
                    rawData = rawData,
                )
            }

            SSE_EVENT_CONNECT,
            SSE_EVENT_PING,
            -> null

            else -> null
        }
    }

    @Serializable
    private data class BadgeEventPayload(
        val badgeCode: String? = null,
        val badgeName: String? = null,
        val introduction: String? = null,
        val imageUrl: String? = null,
        val message: String? = null,
    )

    private const val SSE_EVENT_CONNECT = "connect"
    private const val SSE_EVENT_PING = "ping"
    private const val SSE_EVENT_BADGE_UNLOCKED = "badge_unlocked"
}
