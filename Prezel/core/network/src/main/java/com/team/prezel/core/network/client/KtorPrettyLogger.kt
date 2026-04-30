package com.team.prezel.core.network.client

import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import timber.log.Timber

internal class KtorPrettyLogger(
    private val json: Json,
) : Logger {
    override fun log(message: String) {
        Timber.tag(TAG).d(message.formatJsonLogMessage())
    }

    private fun String.formatJsonLogMessage(): String =
        toPrettyJsonOrNull()
            ?: lineSequence()
                .joinToString(separator = "\n") { line ->
                    line.toPrettyJsonOrNull() ?: line
                }

    private fun String.toPrettyJsonOrNull(): String? {
        val candidate = trim()
        if (!candidate.isJsonObjectOrArray()) return null

        return runCatching {
            val jsonElement = json.parseToJsonElement(candidate)
            json.encodeToString(JsonElement.serializer(), jsonElement)
        }.getOrNull()
    }

    private fun String.isJsonObjectOrArray(): Boolean = (startsWith("{") && endsWith("}")) || (startsWith("[") && endsWith("]"))

    private companion object {
        const val TAG = "KTOR-LOG"
    }
}
