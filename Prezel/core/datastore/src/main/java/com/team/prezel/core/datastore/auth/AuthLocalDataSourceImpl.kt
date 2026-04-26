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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : AuthLocalDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = applicationScope,
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) },
        )

    override fun getToken(): Flow<AuthToken?> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }.map { preferences ->
                preferences.toAuthToken()
            }

    override suspend fun saveToken(token: AuthToken): Result<Unit> =
        runSuspendCatching {
            dataStore.edit { preferences ->
                preferences[KEY_AUTH_TOKEN] = json.encodeToString(token)
            }
        }

    override suspend fun clear(): Result<Unit> =
        runSuspendCatching {
            dataStore.edit { preferences ->
                preferences.remove(KEY_AUTH_TOKEN)
            }
        }

    private suspend inline fun runSuspendCatching(crossinline block: suspend () -> Unit): Result<Unit> =
        try {
            block()
            Result.success(Unit)
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            Result.failure(t)
        }

    private fun Preferences.toAuthToken(): AuthToken? =
        this[KEY_AUTH_TOKEN]
            ?.let { tokenJson ->
                runCatching { json.decodeFromString<AuthToken>(tokenJson) }.getOrNull()
            }?.takeIf { token -> token.accessToken.isNotBlank() && token.refreshToken.isNotBlank() }

    private companion object {
        const val PREFERENCES_NAME = "auth_token_preferences"
        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}
