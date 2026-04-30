package com.team.prezel

import android.app.Application
import com.team.prezel.core.auth.AuthInitializer
import com.team.prezel.util.PrettyLoggerTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PrezelApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(PrettyLoggerTree())
        AuthInitializer.init(this)
    }
}
