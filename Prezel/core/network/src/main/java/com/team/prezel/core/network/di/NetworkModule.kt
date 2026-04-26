package com.team.prezel.core.network.di

import android.os.Build
import com.team.prezel.core.network.ApiResponseConverterFactory
import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.auth.AuthRequestAttributes
import com.team.prezel.core.network.auth.AuthTokenRefresher
import com.team.prezel.core.network.auth.AuthTokenStore
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import timber.log.Timber
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
        }

    @Provides
    @Singleton
    internal fun provideHttpClient(
        json: Json,
        authTokenStore: AuthTokenStore,
        authTokenRefresher: AuthTokenRefresher,
    ): HttpClient = createHttpClient(json) { configureAuthenticatedClient(authTokenStore, authTokenRefresher) }

    @Provides
    @Singleton
    fun provideKtorfit(httpClient: HttpClient): Ktorfit = createKtorfit(httpClient)

    @Provides
    @Singleton
    internal fun provideAuthService(ktorfit: Ktorfit): AuthService = ktorfit.createAuthService()

    private fun createHttpClient(
        json: Json,
        configure: HttpClientConfig<*>.() -> Unit = {},
    ): HttpClient =
        HttpClient(OkHttp) {
            configureBaseClient(json)
            configure()
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

    private fun HttpClientConfig<*>.configureAuthenticatedClient(
        authTokenStore: AuthTokenStore,
        authTokenRefresher: AuthTokenRefresher,
    ) {
        install(Auth) {
            bearer {
                cacheTokens = false
                loadTokens {
                    authTokenStore.toBearerTokens()
                }

                refreshTokens {
                    authTokenRefresher.refreshBearerTokens(this)
                }

                sendWithoutRequest { request ->
                    request.attributes.getOrNull(AuthRequestAttributes.SkipAuthKey) != true
                }
            }
        }
    }

    private suspend fun AuthTokenStore.toBearerTokens(): BearerTokens? {
        val token = getToken().first() ?: return null

        return BearerTokens(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
    }

    private fun createKtorfit(httpClient: HttpClient): Ktorfit =
        Ktorfit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .httpClient(httpClient)
            .converterFactories(ApiResponseConverterFactory())
            .build()

    private fun HttpClientConfig<*>.configureBaseClient(json: Json) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
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

    private fun buildUserAgent(): String =
        buildString {
            append("Prezel-Android/${BuildConfig.BUILD_TYPE} ")
            append("(Android ${Build.VERSION.RELEASE}; ")
            append("SDK ${Build.VERSION.SDK_INT}; ")
            append("${Build.MANUFACTURER} ${Build.MODEL})")
        }
}
