package com.team.prezel.core.network.di

import android.os.Build
import com.team.prezel.core.datastore.auth.AuthTokenStore
import com.team.prezel.core.network.ApiResponseConverterFactory
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.auth.AuthPathPolicy
import com.team.prezel.core.network.auth.AuthTokenRefresher
import com.team.prezel.core.network.service.AuthService
import com.team.prezel.core.network.service.createAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
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
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = false
        }

    @Provides
    @Singleton
    internal fun provideHttpClient(
        json: Json,
        authTokenStore: AuthTokenStore,
        authTokenRefresher: AuthTokenRefresher,
    ): HttpClient =
        HttpClient(OkHttp) {
            configureBaseClient(json)

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = authTokenStore.getAccessToken()
                        if (accessToken.isNullOrBlank()) {
                            null
                        } else {
                            BearerTokens(
                                accessToken = accessToken,
                                refreshToken = authTokenStore.getRefreshToken().orEmpty(),
                            )
                        }
                    }

                    refreshTokens {
                        val refreshedAccessToken = authTokenRefresher.refreshAccessToken() ?: return@refreshTokens null
                        BearerTokens(
                            accessToken = refreshedAccessToken,
                            refreshToken = authTokenStore.getRefreshToken().orEmpty(),
                        )
                    }

                    sendWithoutRequest { request ->
                        AuthPathPolicy.requiresAuthorization(request.url.encodedPath)
                    }
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

    @Provides
    @Singleton
    @Named("refresh")
    fun provideRefreshHttpClient(json: Json): HttpClient =
        HttpClient(OkHttp) {
            configureBaseClient(json)

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

    @Provides
    @Singleton
    fun provideKtorfit(httpClient: HttpClient): Ktorfit =
        Ktorfit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .httpClient(httpClient)
            .converterFactories(ApiResponseConverterFactory())
            .build()

    @Provides
    @Singleton
    @Named("refresh")
    fun provideRefreshKtorfit(
        @Named("refresh") httpClient: HttpClient,
    ): Ktorfit =
        Ktorfit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .httpClient(httpClient)
            .converterFactories(ApiResponseConverterFactory())
            .build()

    @Provides
    @Singleton
    internal fun provideAuthService(ktorfit: Ktorfit): AuthService = ktorfit.createAuthService()

    @Provides
    @Singleton
    @Named("refresh")
    internal fun provideRefreshAuthService(
        @Named("refresh") ktorfit: Ktorfit,
    ): AuthService = ktorfit.createAuthService()

    private fun HttpClientConfig<*>.configureBaseClient(json: Json) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
            connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
            socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
        }

        install(UserAgent) {
            agent = buildUserAgent()
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("KtorClient").d(message)
                }
            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
            level = if (BuildConfig.DEBUG) LogLevel.HEADERS else LogLevel.NONE
        }
    }

    private const val REQUEST_TIMEOUT_MILLIS = 15_000L
    private const val CONNECT_TIMEOUT_MILLIS = 10_000L
    private const val SOCKET_TIMEOUT_MILLIS = 15_000L

    private fun buildUserAgent(): String =
        "Prezel-Android/${BuildConfig.BUILD_TYPE} (Android ${Build.VERSION.RELEASE}; SDK ${Build.VERSION.SDK_INT}; ${Build.MANUFACTURER} ${Build.MODEL})"
}
