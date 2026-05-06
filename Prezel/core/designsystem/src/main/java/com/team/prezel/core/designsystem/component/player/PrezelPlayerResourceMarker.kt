package com.team.prezel.core.designsystem.component.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
enum class PrezelPlayerResourceMarkerType {
    GOOD,
    WARNING,
    NEUTRAL,
}

internal val PlayerMarkerSize = 8.dp

@Composable
fun PrezelPlayerResourceMarker(
    type: PrezelPlayerResourceMarkerType,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(PlayerMarkerSize)
            .clip(PrezelTheme.shapes.V1000)
            .background(type.color),
    )
}

val PrezelPlayerResourceMarkerType.color: Color
    @Composable
    get() = when (this) {
        PrezelPlayerResourceMarkerType.GOOD -> PrezelTheme.colors.feedbackGoodRegular
        PrezelPlayerResourceMarkerType.WARNING -> PrezelTheme.colors.feedbackWarningRegular
        PrezelPlayerResourceMarkerType.NEUTRAL -> PrezelTheme.colors.iconRegular
    }

@Preview(showBackground = true)
@Composable
private fun PrezelPlayerResourceMarkerPreview() {
    PrezelTheme {
        PreviewSection(title = "Player Resource Marker") {
            Row(horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16)) {
                PrezelPlayerResourceMarker(type = PrezelPlayerResourceMarkerType.GOOD)
                PrezelPlayerResourceMarker(type = PrezelPlayerResourceMarkerType.WARNING)
                PrezelPlayerResourceMarker(type = PrezelPlayerResourceMarkerType.NEUTRAL)
            }
        }
    }
}
