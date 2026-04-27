package com.team.prezel.core.common.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultGlobalEventBus @Inject constructor() : GlobalEventBus {
    private val _events = MutableSharedFlow<GlobalEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val events: Flow<GlobalEvent> = _events.asSharedFlow()

    override suspend fun emit(event: GlobalEvent) {
        _events.emit(event)
    }
}
