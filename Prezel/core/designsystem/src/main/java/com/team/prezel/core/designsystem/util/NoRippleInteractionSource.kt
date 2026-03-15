package com.team.prezel.core.designsystem.util

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object NoRippleInteractionSource : MutableInteractionSource {
    override suspend fun emit(interaction: Interaction) = Unit

    override fun tryEmit(interaction: Interaction): Boolean = true

    override val interactions: Flow<Interaction> = emptyFlow()
}
