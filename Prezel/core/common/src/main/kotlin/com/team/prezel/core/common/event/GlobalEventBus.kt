package com.team.prezel.core.common.event

import kotlinx.coroutines.flow.Flow

interface GlobalEventBus {
    val events: Flow<GlobalEvent>

    suspend fun emit(event: GlobalEvent)
}
