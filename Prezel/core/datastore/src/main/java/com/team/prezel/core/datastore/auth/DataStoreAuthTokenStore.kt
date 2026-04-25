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
import com.team.prezel.core.model.auth.AuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
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

    override suspend fun getToken(): AuthToken? {
        val preferences = readPreferences()
        val accessToken = preferences[KEY_ACCESS_TOKEN]
        val refreshToken = preferences[KEY_REFRESH_TOKEN]
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) return null

        return AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    override suspend fun saveToken(token: AuthToken) {
        dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token.accessToken
            preferences[KEY_REFRESH_TOKEN] = token.refreshToken
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
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
