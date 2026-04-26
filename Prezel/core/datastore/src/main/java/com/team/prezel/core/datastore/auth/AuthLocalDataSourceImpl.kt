package com.team.prezel.core.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team.prezel.core.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class AuthLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AuthLocalDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    private val cacheMutex = Mutex()
    private val cachedToken = MutableStateFlow<AuthToken?>(null)

    @Volatile
    private var isCacheInitialized = false

    override fun getToken(): Flow<AuthToken?> =
        flow {
            ensureCacheInitialized()
            emitAll(cachedToken)
        }

    override suspend fun saveToken(token: AuthToken): Result<Unit> =
        runSuspendCatching {
            dataStore.edit { preferences ->
                preferences[KEY_AUTH_TOKEN] = json.encodeToString(token)
            }
        }.onSuccess {
            updateCache(token)
        }

    override suspend fun clear(): Result<Unit> =
        runSuspendCatching {
            dataStore.edit { preferences ->
                preferences.remove(KEY_AUTH_TOKEN)
            }
        }.onSuccess {
            updateCache(null)
        }

    private suspend fun ensureCacheInitialized() {
        if (isCacheInitialized) return

        cacheMutex.withLock {
            if (isCacheInitialized) return

            cachedToken.value = readTokenFromDataStore()
            isCacheInitialized = true
        }
    }

    private suspend fun readTokenFromDataStore(): AuthToken? =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }.map { preferences ->
                preferences.toAuthToken()
            }.first()

    private fun updateCache(token: AuthToken?) {
        cachedToken.value = token
        isCacheInitialized = true
    }

    private suspend inline fun runSuspendCatching(crossinline block: suspend () -> Unit): Result<Unit> =
        try {
            block()
            Result.success(Unit)
        } catch (t: CancellationException) {
            throw t
        } catch (t: Throwable) {
            Result.failure(t)
        }

    private fun Preferences.toAuthToken(): AuthToken? =
        this[KEY_AUTH_TOKEN]
            ?.let { tokenJson ->
                runCatching { json.decodeFromString<AuthToken>(tokenJson) }.getOrNull()
            }?.takeIf { token -> token.accessToken.isNotBlank() && token.refreshToken.isNotBlank() }

    private companion object {
        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}
