package com.team.prezel.core.data.auth

import com.team.prezel.core.domain.session.AuthSessionEventPublisher
import com.team.prezel.core.network.auth.AuthSessionExpiredNotifier
import javax.inject.Inject

internal class DataAuthSessionExpiredNotifier @Inject constructor(
    private val authSessionEventPublisher: AuthSessionEventPublisher,
) : AuthSessionExpiredNotifier {
    override fun notifySessionExpired() {
        authSessionEventPublisher.notifySessionExpired()
    }
}
