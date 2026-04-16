package com.team.prezel.core.model.profile

@JvmInline
value class Nickname private constructor(
    val value: String,
) {
    sealed interface CreationResult {
        data class Success(
            val nickname: Nickname,
        ) : CreationResult

        data class Failure(
            val reason: InvalidReason,
        ) : CreationResult
    }

    enum class InvalidReason {
        TOO_SHORT,
        TOO_LONG,
        INVALID_CHARACTER,
    }

    companion object {
        const val MAX_LENGTH = 10
        private const val MIN_LENGTH = 2
        private const val LATIN_UPPERCASE_START = 'A'.code
        private const val LATIN_UPPERCASE_END = 'Z'.code
        private const val LATIN_LOWERCASE_START = 'a'.code
        private const val LATIN_LOWERCASE_END = 'z'.code

        fun create(value: String): CreationResult =
            when (val reason = invalidReasonOf(value)) {
                null -> CreationResult.Success(Nickname(value))
                else -> CreationResult.Failure(reason)
            }

        private fun invalidReasonOf(value: String): InvalidReason? {
            val length = value.codePointCount(0, value.length)

            return when {
                length < MIN_LENGTH -> InvalidReason.TOO_SHORT
                length > MAX_LENGTH -> InvalidReason.TOO_LONG
                !value.codePoints().allMatch(::isAllowedCodePoint) -> InvalidReason.INVALID_CHARACTER
                else -> null
            }
        }

        private fun isAllowedCodePoint(codePoint: Int): Boolean =
            codePoint in LATIN_UPPERCASE_START..LATIN_UPPERCASE_END ||
                codePoint in LATIN_LOWERCASE_START..LATIN_LOWERCASE_END ||
                Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HANGUL
    }
}
