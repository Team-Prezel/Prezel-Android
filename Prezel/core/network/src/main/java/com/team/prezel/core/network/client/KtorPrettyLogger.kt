package com.team.prezel.core.network.client

import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import timber.log.Timber

@OptIn(ExperimentalSerializationApi::class)
internal object KtorPrettyLogger : Logger {
    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "\t"
        isLenient = true
    }

    override fun log(message: String) {
        Timber.tag(TAG).d(message.toPrettyLogMessage())
    }

    private fun String.toPrettyLogMessage(): String =
        parsePrettyJson() ?: lines()
            .joinToString(separator = "\n") { line ->
                line.parsePrettyJson() ?: line
            }

    private fun String.parsePrettyJson(): String? {
        val candidate = trim()

        if (!candidate.looksLikeJson()) return null

        return runCatching {
            val element = json.parseToJsonElement(candidate)
            json.encodeToString(element)
        }.getOrNull()
    }

    private fun String.looksLikeJson(): Boolean =
        (startsWith("{") && endsWith("}")) ||
            (startsWith("[") && endsWith("]"))

    private const val TAG = "KTOR-LOG"
}
