package com.team.prezel.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.team.prezel.core.domain.repository.fcm.FcmTokenProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseFcmTokenProvider @Inject constructor() : FcmTokenProvider {
    @Suppress("DEPRECATION")
    override suspend fun getToken(): Result<String> =
        suspendCancellableCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .token
                .addOnCompleteListener { task ->
                    if (!continuation.isActive) return@addOnCompleteListener

                    val result = when {
                        task.isSuccessful -> task.result.toFcmTokenResult()
                        else -> Result.failure(task.exception ?: IllegalStateException("Fetching FCM token failed."))
                    }
                    continuation.resume(result)
                }
        }

    private fun String?.toFcmTokenResult(): Result<String> =
        if (isNullOrBlank()) {
            Result.failure(IllegalStateException("Fetched FCM token is blank."))
        } else {
            Result.success(this)
        }
}
