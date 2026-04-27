package com.team.prezel.core.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team.prezel.core.model.auth.AuthTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AuthLocalDataSource {
    override val tokens: Flow<AuthTokens?>
        get() = dataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }.map { preferences ->
                val accessToken = preferences[ACCESS_TOKEN_KEY]
                val refreshToken = preferences[REFRESH_TOKEN_KEY]

                if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                    null
                } else {
                    AuthTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                    )
                }
            }

    override suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    private companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("auth_access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("auth_refresh_token")
    }
}
