package com.team.prezel.core.designsystem.component.player

import androidx.compose.runtime.Immutable

enum class PrezelPlayerMarkerType {
    GOOD,
    WARNING,
    NEUTRAL,
}

@Immutable
sealed interface PrezelPlayerItem {
    val timeMillis: Long

    @Immutable
    data class Segment(
        override val timeMillis: Long,
    ) : PrezelPlayerItem

    @Immutable
    data class Marker(
        override val timeMillis: Long,
        val markerType: PrezelPlayerMarkerType,
    ) : PrezelPlayerItem
}
