package com.team.prezel.core.network.client

import android.os.Build
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.model.auth.AuthTokens
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.auth.AuthRequestAttributes
import com.team.prezel.core.network.auth.TokenProvider
import com.team.prezel.core.network.model.auth.reissue.ReissueRequest
import com.team.prezel.core.network.model.auth.reissue.ReissueResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.service.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class HttpClientFactory @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authServiceProvider: Provider<AuthService>,
    private val globalEventBus: GlobalEventBus,
) {
    private val networkJson: Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        }

    private val ktorLogger: Logger =
        object : Logger {
            override fun log(message: String) {
                Timber.tag("KTOR-LOG").d(message)
            }
        }

    fun create(
        block: HttpClientConfig<*>.() -> Unit = {
            configureDefaultRequest()
            installContentNegotiation()
            installUserAgent()
            installLogging()
            installAuth()
        },
    ): HttpClient = HttpClient(OkHttp) { block() }

    internal fun HttpClientConfig<*>.configureDefaultRequest() {
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    internal fun HttpClientConfig<*>.installContentNegotiation() {
        install(ContentNegotiation) {
            json(networkJson)
        }
    }

    internal fun HttpClientConfig<*>.installUserAgent() {
        install(UserAgent) {
            agent = buildUserAgent()
        }
    }

    internal fun HttpClientConfig<*>.installLogging() {
        install(Logging) {
            logger = ktorLogger
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
            level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
        }
    }

    internal fun HttpClientConfig<*>.installAuth() {
        install(Auth) {
            bearer {
                cacheTokens = true

                loadTokens { tokenProvider.getTokens()?.toBearerTokens() }

                refreshTokens { refreshBearerTokens() }

                sendWithoutRequest { request ->
                    request.attributes.getOrNull(AuthRequestAttributes.SkipAuthKey) != true
                }
            }
        }
    }

    private suspend fun RefreshTokensParams.refreshBearerTokens(): BearerTokens? {
        val refreshToken = oldTokens?.refreshToken ?: return null

        return try {
            authServiceProvider
                .get()
                .reissue(request = ReissueRequest(refreshToken))
                .requireData()
                .let { response ->
                    tokenProvider.updateTokens(accessToken = response.accessToken, refreshToken = response.refreshToken)
                    client.clearAuthTokens()
                    response.toBearerTokens()
                }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            tokenProvider.clearTokens()
            client.clearAuthTokens()
            globalEventBus.emit(GlobalEvent.ForceLogout)
            null
        }
    }

    private fun AuthTokens.toBearerTokens(): BearerTokens =
        BearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private fun ReissueResponse.toBearerTokens(): BearerTokens =
        BearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private fun buildUserAgent(): String =
        buildString {
            append("Prezel-Android/${BuildConfig.BUILD_TYPE} ")
            append("(Android ${Build.VERSION.RELEASE}; ")
            append("SDK ${Build.VERSION.SDK_INT}; ")
            append("${Build.MANUFACTURER} ${Build.MODEL})")
        }
}
