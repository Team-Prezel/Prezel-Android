package com.team.prezel.core.network.client

import android.os.Build
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.auth.AuthRequestAttributes
import com.team.prezel.core.network.auth.TokenProvider
import com.team.prezel.core.network.model.ApiException
import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.ServerErrorCode
import com.team.prezel.core.network.model.auth.reissue.ReissueRequest
import com.team.prezel.core.network.model.auth.reissue.ReissueResponse
import com.team.prezel.core.network.model.requireData
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
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
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HttpClientFactory @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val globalEventBus: GlobalEventBus,
) {
    private val networkJson: Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
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
            logger = KtorPrettyLogger
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
            client
                .post("auth/reissue") {
                    markAsRefreshTokenRequest()
                    contentType(ContentType.Application.Json)
                    setBody(ReissueRequest(refreshToken))
                    attributes.put(AuthRequestAttributes.SkipAuthKey, true)
                }.body<BaseResponse<ReissueResponse>>()
                .requireData()
                .let { newTokens ->
                    tokenProvider.updateTokens(
                        accessToken = newTokens.accessToken,
                        refreshToken = newTokens.refreshToken,
                    )
                    newTokens.toBearerTokens()
                }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e)
            handleExpiredRefreshToken(client = client, exception = e)
            null
        }
    }

    private suspend fun handleExpiredRefreshToken(
        client: HttpClient,
        exception: Exception,
    ) {
        if (!exception.isSessionInvalidForReissue()) return

        tokenProvider.clearTokens()
        client.clearAuthTokens()
        globalEventBus.emit(GlobalEvent.ForceLogout)
    }

    private fun TokenProvider.AuthTokens.toBearerTokens(): BearerTokens =
        BearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private fun ReissueResponse.toBearerTokens(): BearerTokens =
        BearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private fun Throwable.isSessionInvalidForReissue(): Boolean =
        this is ApiException &&
            errorCode in listOf(
                ServerErrorCode.INVALID_TOKEN,
                ServerErrorCode.TOKEN_STOLEN,
                ServerErrorCode.USER_NOT_FOUND,
            )

    private fun buildUserAgent(): String =
        buildString {
            append("Prezel-Android/${BuildConfig.BUILD_TYPE} ")
            append("(Android ${Build.VERSION.RELEASE}; ")
            append("SDK ${Build.VERSION.SDK_INT}; ")
            append("${Build.MANUFACTURER} ${Build.MODEL})")
        }
}
