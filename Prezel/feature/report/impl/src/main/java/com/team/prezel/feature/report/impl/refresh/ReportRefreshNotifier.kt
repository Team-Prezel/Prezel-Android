package com.team.prezel.feature.report.impl.refresh

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ReportRefreshNotifier @Inject constructor() {
    private val _events = MutableSharedFlow<Long>(
        extraBufferCapacity = 1,
    )

    val events: SharedFlow<Long> = _events.asSharedFlow()

    fun requestRefresh(presentationId: Long) {
        _events.tryEmit(presentationId)
    }
}
