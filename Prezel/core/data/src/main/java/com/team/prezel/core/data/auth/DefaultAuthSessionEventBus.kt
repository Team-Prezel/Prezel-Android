package com.team.prezel.core.data.auth

import com.team.prezel.core.domain.session.AuthSessionEvent
import com.team.prezel.core.domain.session.AuthSessionEventPublisher
import com.team.prezel.core.domain.session.AuthSessionEventStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultAuthSessionEventBus @Inject constructor() :
    AuthSessionEventPublisher,
    AuthSessionEventStream {
        private val event = MutableStateFlow<AuthSessionEvent?>(null)
        override val events: Flow<AuthSessionEvent> = event.filterNotNull()

        override fun notifySessionExpired() {
            event.value = AuthSessionEvent.Expired
        }

        override fun clearSessionExpiredEvent() {
            event.value = null
        }
    }
