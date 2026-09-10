package com.team.prezel.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.usecase.fcm.SaveFcmTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PrezelFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var saveFcmTokenUseCase: SaveFcmTokenUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        serviceScope.launch {
            authRepository
                .hasJwtToken()
                .onSuccess { hasJwtToken ->
                    if (!hasJwtToken) return@onSuccess

                    saveFcmTokenUseCase(token = token)
                        .onFailure { exception -> Timber.e(exception, "Failed to save refreshed FCM token.") }
                }.onFailure { exception ->
                    Timber.e(exception, "Failed to check auth state before saving refreshed FCM token.")
                }
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
