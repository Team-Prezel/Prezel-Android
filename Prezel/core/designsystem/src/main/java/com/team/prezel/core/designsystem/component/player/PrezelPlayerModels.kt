package com.team.prezel.core.designsystem.component.player

import androidx.compose.runtime.Immutable

enum class PrezelPlayerResourceTrackType {
    SPEECH,
    SCRIPT_MATCH,
}

enum class PrezelPlayerResourceMarkerType {
    GOOD,
    WARNING,
    NEUTRAL,
}

@Immutable
data class PrezelPlayerItem(
    val id: String,
    val startMillis: Long,
)

@Immutable
data class PrezelPlayerResourceMarkerItem(
    val timeSeconds: Long,
    val trackType: PrezelPlayerResourceTrackType,
    val markerType: PrezelPlayerResourceMarkerType,
)
