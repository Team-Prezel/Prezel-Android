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
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    private val authTokenCacheInvalidator: AuthTokenCacheInvalidator,
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

    override suspend fun saveToken(
        token: AuthToken,
        invalidateCache: Boolean,
    ): Result<Unit> {
        val result = runSuspendCatching {
            dataStore.edit { preferences ->
                preferences[KEY_AUTH_TOKEN] = json.encodeToString(token)
            }
        }
        if (result.isSuccess && invalidateCache) invalidateAuthTokenCaches()
        return result
    }

    override suspend fun clear(): Result<Unit> {
        val result = runSuspendCatching {
            dataStore.edit { preferences ->
                preferences.remove(KEY_AUTH_TOKEN)
            }
        }
        if (result.isSuccess) invalidateAuthTokenCaches()
        return result
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

    private fun invalidateAuthTokenCaches() {
        try {
            authTokenCacheInvalidator.invalidate()
        } catch (t: CancellationException) {
            throw t
        } catch (t: Throwable) {
            Timber.e(t, "인증 토큰 캐시 무효화에 실패했습니다.")
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "auth_token_preferences"
        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}
