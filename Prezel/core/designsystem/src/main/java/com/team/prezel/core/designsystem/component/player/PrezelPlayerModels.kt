package com.team.prezel.core.designsystem.component.player

import androidx.compose.runtime.Immutable

enum class PrezelPlayerResourceTrackType {
    SPEECH,
    SCRIPT_MATCH,
}

enum class PrezelSpeechMarkerType {
    GOOD,
    WARNING,
}

enum class PrezelScriptMatchMarkerType {
    GOOD,
    NEUTRAL,
}

@Immutable
sealed interface PrezelPlayerResourceMarkerItem {
    val timeSeconds: Long

    @Immutable
    data class Speech(
        override val timeSeconds: Long,
        val type: PrezelSpeechMarkerType,
    ) : PrezelPlayerResourceMarkerItem

    @Immutable
    data class ScriptMatch(
        override val timeSeconds: Long,
        val type: PrezelScriptMatchMarkerType,
    ) : PrezelPlayerResourceMarkerItem

    companion object {
        fun speech(
            timeSeconds: Long,
            type: PrezelSpeechMarkerType,
        ): PrezelPlayerResourceMarkerItem =
            Speech(
                timeSeconds = timeSeconds,
                type = type,
            )

        fun scriptMatch(
            timeSeconds: Long,
            type: PrezelScriptMatchMarkerType,
        ): PrezelPlayerResourceMarkerItem =
            ScriptMatch(
                timeSeconds = timeSeconds,
                type = type,
            )
    }
}
