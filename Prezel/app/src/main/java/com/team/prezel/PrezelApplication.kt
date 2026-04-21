package com.team.prezel

import android.app.Application
import com.team.prezel.core.auth.AuthInitializer
import com.team.prezel.core.datastore.auth.AuthTokenStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PrezelApplication : Application() {
    @Inject
    lateinit var authTokenStore: AuthTokenStore

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Eagerly create the token store so its background initialization starts at app launch.
        authTokenStore.getAccessToken()

        AuthInitializer.init(this)
    }
}
