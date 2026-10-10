package com.team.prezel.core.common.fcm

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRefreshNotifier @Inject constructor() {
    private val _events = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    val events: Flow<Unit> = _events.asSharedFlow()

    fun requestRefresh() {
        _events.tryEmit(Unit)
    }
}
