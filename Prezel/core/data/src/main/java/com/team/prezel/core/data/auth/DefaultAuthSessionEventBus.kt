package com.team.prezel.core.data.auth

import com.team.prezel.core.domain.session.AuthSessionEvent
import com.team.prezel.core.domain.session.AuthSessionEventPublisher
import com.team.prezel.core.domain.session.AuthSessionEventStream
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultAuthSessionEventBus @Inject constructor() :
    AuthSessionEventPublisher,
    AuthSessionEventStream {
        private val _events = MutableSharedFlow<AuthSessionEvent>(extraBufferCapacity = 1)
        override val events: SharedFlow<AuthSessionEvent> = _events.asSharedFlow()

        override fun notifySessionExpired() {
            _events.tryEmit(AuthSessionEvent.Expired)
        }
    }
