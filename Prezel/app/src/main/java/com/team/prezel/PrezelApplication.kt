package com.team.prezel

import android.app.Application
import com.team.prezel.core.auth.AuthInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PrezelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AuthInitializer.init(this)
    }
}
