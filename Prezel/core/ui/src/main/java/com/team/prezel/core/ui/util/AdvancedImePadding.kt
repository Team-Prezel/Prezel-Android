package com.team.prezel.core.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.roundToInt

fun Modifier.advancedImePadding() =
    composed {
        var consumePadding by remember { mutableIntStateOf(0) }
        onGloballyPositioned { coordinates ->
            consumePadding = coordinates.findRootCoordinates().size.height -
                (coordinates.positionInRoot().y + coordinates.size.height).roundToInt()
        }.consumeWindowInsets(
            PaddingValues(bottom = with(LocalDensity.current) { consumePadding.toDp() }),
        ).imePadding()
    }
