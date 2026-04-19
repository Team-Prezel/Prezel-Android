package com.team.prezel.core.datastore.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.team.prezel.core.datastore.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.Job
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStoreAuthTokenStore @Inject constructor(
    @ApplicationContext context: Context,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : AuthTokenStore {
    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = applicationScope,
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) },
        )

    @Volatile
    private var accessToken: String? = null

    @Volatile
    private var refreshToken: String? = null

    private val mutex = Mutex()
    private val initializationJob: Job =
        applicationScope.launch {
            mutex.withLock {
                val preferences = readPreferences()
                accessToken = preferences[KEY_ACCESS_TOKEN]
                refreshToken = preferences[KEY_REFRESH_TOKEN]
            }
        }

    override fun getAccessToken(): String? = accessToken

    override fun getRefreshToken(): String? = refreshToken

    override suspend fun awaitInitialized() {
        initializationJob.join()
    }

    override suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        awaitInitialized()
        mutex.withLock {
            dataStore.edit { preferences ->
                preferences[KEY_ACCESS_TOKEN] = accessToken
                preferences[KEY_REFRESH_TOKEN] = refreshToken
            }
            this.accessToken = accessToken
            this.refreshToken = refreshToken
        }
    }

    override suspend fun clear() {
        awaitInitialized()
        mutex.withLock {
            dataStore.edit { preferences ->
                preferences.remove(KEY_ACCESS_TOKEN)
                preferences.remove(KEY_REFRESH_TOKEN)
            }
            accessToken = null
            refreshToken = null
        }
    }

    private suspend fun readPreferences(): Preferences =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }.first()

    private companion object {
        const val PREFERENCES_NAME = "auth_token_preferences"
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
