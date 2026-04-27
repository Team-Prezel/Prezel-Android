package com.team.prezel.core.network.di

import android.os.Build
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.auth.AuthRequestAttributes
import com.team.prezel.core.network.auth.TokenProvider
import com.team.prezel.core.network.model.auth.reissue.ReissueRequest
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.service.AuthService
import com.team.prezel.core.network.service.createAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.plugins.auth.providers.BearerTokens
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
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val networkJson: Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        }

    @Provides
    @Singleton
    internal fun provideHttpClient(
        tokenProvider: TokenProvider,
        authServiceProvider: Provider<AuthService>,
        globalEventBus: GlobalEventBus,
    ): HttpClient =
        HttpClient(OkHttp) {
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) { json(networkJson) }

            install(UserAgent) { agent = buildUserAgent() }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("KTOR-LOG").d(message)
                    }
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }

            install(Auth) {
                bearer {
                    cacheTokens = true
                    loadTokens {
                        tokenProvider.getTokens()?.let { tokens ->
                            BearerTokens(
                                accessToken = tokens.accessToken,
                                refreshToken = tokens.refreshToken,
                            )
                        }
                    }

                    refreshTokens {
                        val oldRefreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
                        return@refreshTokens try {
                            val response = authServiceProvider
                                .get()
                                .reissue(request = ReissueRequest(oldRefreshToken))
                                .requireData()
                            with(response) {
                                tokenProvider.updateTokens(accessToken = accessToken, refreshToken = refreshToken)
                                BearerTokens(accessToken = accessToken, refreshToken = refreshToken).also { client.clearAuthTokens() }
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

                    sendWithoutRequest { request ->
                        request.attributes.getOrNull(AuthRequestAttributes.SkipAuthKey) != true
                    }
                }
            }
        }

    @Provides
    @Singleton
    fun provideKtorfit(httpClient: HttpClient): Ktorfit =
        Ktorfit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .httpClient(httpClient)
            .build()

    @Provides
    @Singleton
    internal fun provideAuthService(ktorfit: Ktorfit): AuthService = ktorfit.createAuthService()

    private fun buildUserAgent(): String =
        buildString {
            append("Prezel-Android/${BuildConfig.BUILD_TYPE} ")
            append("(Android ${Build.VERSION.RELEASE}; ")
            append("SDK ${Build.VERSION.SDK_INT}; ")
            append("${Build.MANUFACTURER} ${Build.MODEL})")
        }
}
