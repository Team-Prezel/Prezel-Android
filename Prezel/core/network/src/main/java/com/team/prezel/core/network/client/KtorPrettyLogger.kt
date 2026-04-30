package com.team.prezel.core.network.client

import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import timber.log.Timber

@OptIn(ExperimentalSerializationApi::class)
internal object KtorPrettyLogger : Logger {
    private const val TAG = "NETWORK"
    private const val REQUEST = "REQUEST:"
    private const val RESPONSE = "RESPONSE:"
    private const val REQUEST_EMOJI = "🚀"
    private const val RESPONSE_EMOJI = "📥"
    private const val COMMON_HEADERS = "COMMON HEADERS"
    private const val CONTENT_HEADERS = "CONTENT HEADERS"
    private const val BODY_START = "BODY START"
    private const val BODY_END = "BODY END"

    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "\t"
        isLenient = true
    }

    override fun log(message: String) {
        Timber.tag(TAG).d(
            message
                .prettifyBodyJson()
                .formatSections(),
        )
    }

    private fun String.prettifyBodyJson(): String {
        val original = this

        val bodyStartIndex = original.indexOf(BODY_START)
        if (bodyStartIndex == -1) return original

        val bodyEndIndex = original.indexOf(BODY_END, startIndex = bodyStartIndex)
        if (bodyEndIndex == -1) return original

        val body = original
            .substring(
                startIndex = bodyStartIndex + BODY_START.length,
                endIndex = bodyEndIndex,
            ).trim()

        val prettyBody = body.parsePrettyJson() ?: body

        return buildString {
            append(original.substring(startIndex = 0, endIndex = bodyStartIndex))
            appendLine(BODY_START)
            appendLine(prettyBody)
            append(BODY_END)
            append(original.substring(startIndex = bodyEndIndex + BODY_END.length))
        }
    }

    private fun String.formatSections(): String {
        if (!isKtorHttpLog()) return this

        return buildList {
            lines().forEachIndexed { index, line ->
                when {
                    index != 0 && line.isSkipMarker() -> Unit
                    index != 0 && line.isBlankLineMarker() -> add("")
                    else -> add(line.withEmojiHeader())
                }
            }
        }.joinToString(separator = "\n")
    }

    private fun String.isKtorHttpLog(): Boolean = contains(REQUEST) || contains(RESPONSE)

    private fun String.isSkipMarker(): Boolean = startsWith(COMMON_HEADERS) || startsWith(CONTENT_HEADERS) || startsWith(BODY_END)

    private fun String.isBlankLineMarker(): Boolean = startsWith(BODY_START)

    private fun String.withEmojiHeader(): String =
        when {
            startsWith(REQUEST) -> replaceFirst(REQUEST, "$REQUEST_EMOJI $REQUEST")
            startsWith(RESPONSE) -> replaceFirst(RESPONSE, "$RESPONSE_EMOJI $RESPONSE")
            else -> this
        }

    private fun String.parsePrettyJson(): String? {
        val candidate = trim()
        if (!candidate.looksLikeJson()) return null

        return runCatching {
            val element = json.parseToJsonElement(candidate)
            json.encodeToString(element)
        }.getOrNull()
    }

    private fun String.looksLikeJson(): Boolean = (startsWith("{") && endsWith("}")) || (startsWith("[") && endsWith("]"))
}
