package com.team.prezel.fcm

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team.prezel.MainActivity
import com.team.prezel.R
import com.team.prezel.core.common.fcm.AnalysisResultFcmPayload
import com.team.prezel.core.common.fcm.HistoryRefreshNotifier
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

    @Inject
    lateinit var historyRefreshNotifier: HistoryRefreshNotifier

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

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val analysisResultPayload = AnalysisResultFcmPayload.from(message.data)

        if (message.data[AnalysisResultFcmPayload.KEY_TYPE] == AnalysisResultFcmPayload.TYPE_ANALYSIS_RESULT &&
            analysisResultPayload == null
        ) {
            Timber.w("Invalid analysis result FCM payload: ${message.data.toSortedMap()}")
            return
        }

        analysisResultPayload?.let { payload ->
            historyRefreshNotifier.requestRefresh()
            showAnalysisResultNotification(message = message, payload = payload)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun showAnalysisResultNotification(
        message: RemoteMessage,
        payload: AnalysisResultFcmPayload,
    ) {
        if (!canPostNotifications()) {
            return
        }

        val notification = message.notification
        val title = notification?.title ?: if (payload.isCompleted) "분석 완료" else "분석 실패"
        val body = notification?.body.orEmpty()
        val pendingIntent = PendingIntent.getActivity(
            this,
            payload.notificationRequestCode,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putAnalysisResultPayload(payload)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notificationId = payload.notificationRequestCode
        val appNotification = NotificationCompat
            .Builder(this, ANALYSIS_RESULT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_fcm_analysis_result)
            .setColor(ContextCompat.getColor(this, R.color.prezel_notification_color))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(notificationId, appNotification)
    }

    private fun canPostNotifications(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    private val AnalysisResultFcmPayload.notificationRequestCode: Int
        get() = (analysisResultId ?: presentationId).hashCode()
}

fun Intent.putAnalysisResultPayload(payload: AnalysisResultFcmPayload): Intent =
    apply {
        payload.toDataMap().forEach { (key, value) ->
            putExtra(key, value)
        }
    }

fun Intent.getAnalysisResultPayload(): AnalysisResultFcmPayload? {
    val data = AnalysisResultFcmPayload
        .run {
            listOf(
                KEY_TYPE,
                KEY_PRESENTATION_ID,
                KEY_ANALYSIS_RESULT_ID,
                KEY_STATUS,
                KEY_CARD_STATUS,
                KEY_ERROR_TYPE,
                KEY_SCRIPT,
                KEY_AUDIO_URL,
            )
        }.mapNotNull { key ->
            getStringExtra(key)?.let { value -> key to value }
        }.toMap()

    return AnalysisResultFcmPayload.from(data)
}
