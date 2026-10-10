package com.team.prezel.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

internal const val ANALYSIS_RESULT_NOTIFICATION_CHANNEL_ID = "analysis_result"

internal fun Context.createFcmNotificationChannels() {
    val channel = NotificationChannel(
        ANALYSIS_RESULT_NOTIFICATION_CHANNEL_ID,
        "분석 결과",
        NotificationManager.IMPORTANCE_DEFAULT,
    )

    getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
}
