package com.team.prezel.core.network.di

import com.team.prezel.core.network.ApiResponseConverterFactory
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.auth.AuthPathPolicy
import com.team.prezel.core.network.auth.AuthTokenStore
import com.team.prezel.core.network.auth.TokenRefreshAuthenticator
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
    fun provideHttpClient(
        json: Json,
        authTokenStore: AuthTokenStore,
        tokenRefreshAuthenticator: TokenRefreshAuthenticator,
    ): HttpClient =
        HttpClient(OkHttp) {
            engine {
                config {
                    authenticator(tokenRefreshAuthenticator)
                }
            }

            configureBaseClient(json)

            defaultRequest {
                contentType(ContentType.Application.Json)

                if (headers[HttpHeaders.Authorization] == null && AuthPathPolicy.requiresAuthorization(url.encodedPath)) {
                    authTokenStore.getAccessToken()?.let { accessToken ->
                        headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
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
            .converterFactories(ApiResponseConverterFactory())
            .build()

    @Provides
    @Singleton
    internal fun provideAuthService(ktorfit: Ktorfit): AuthService = ktorfit.createAuthService()

    private fun HttpClientConfig<*>.configureBaseClient(json: Json) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
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
}
