package com.team.prezel.core.network

import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.network.auth.AuthSessionExpiredNotifier
import com.team.prezel.core.network.auth.AuthTokenRefresher
import com.team.prezel.core.network.auth.AuthTokenStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

class AuthTokenRefresherTest {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `refresh token 만료 응답이면 토큰을 삭제하고 세션 만료를 알린다`() =
        runTest {
            val tokenStore = FakeAuthTokenStore()
            val notifier = FakeAuthSessionExpiredNotifier()
            val refresher = AuthTokenRefresher(
                json = json,
                authTokenStore = tokenStore,
                authSessionExpiredNotifier = notifier,
            )
            val client = createClient(
                content =
                    """
                    {
                      "status": 401,
                      "code": "T001",
                      "data": null,
                      "message": "Token is invalid."
                    }
                    """.trimIndent(),
                status = HttpStatusCode.Unauthorized,
            )

            val result = refresher.refreshBearerTokens(
                params = client.refreshTokensParams(),
            )

            assertNull(result)
            assertEquals(1, tokenStore.clearCount)
            assertNull(tokenStore.token)
            assertEquals(1, notifier.notifyCount)
        }

    @Test
    fun `refresh 성공이면 새 토큰을 저장하고 BearerTokens를 반환한다`() =
        runTest {
            val tokenStore = FakeAuthTokenStore()
            val notifier = FakeAuthSessionExpiredNotifier()
            val refresher = AuthTokenRefresher(
                json = json,
                authTokenStore = tokenStore,
                authSessionExpiredNotifier = notifier,
            )
            val client = createClient(
                content =
                    """
                    {
                      "accessToken": "new-access-token",
                      "refreshToken": "new-refresh-token"
                    }
                    """.trimIndent(),
                status = HttpStatusCode.OK,
            )

            val result = refresher.refreshBearerTokens(
                params = client.refreshTokensParams(),
            )

            assertEquals("new-access-token", result?.accessToken)
            assertEquals("new-refresh-token", result?.refreshToken)
            assertEquals(AuthToken("new-access-token", "new-refresh-token"), tokenStore.token)
            assertEquals(1, tokenStore.saveCount)
            assertEquals(0, tokenStore.clearCount)
            assertEquals(0, notifier.notifyCount)
        }

    @Test
    fun `저장소 토큰이 이미 갱신됐으면 재발급 요청 없이 최신 BearerTokens를 반환한다`() =
        runTest {
            val updatedToken = AuthToken(
                accessToken = "updated-access-token",
                refreshToken = "updated-refresh-token",
            )
            val tokenStore = FakeAuthTokenStore(token = updatedToken)
            val notifier = FakeAuthSessionExpiredNotifier()
            val refresher = AuthTokenRefresher(
                json = json,
                authTokenStore = tokenStore,
                authSessionExpiredNotifier = notifier,
            )
            val client = createClient(
                content = "",
                status = HttpStatusCode.OK,
                failOnReissue = true,
            )

            val result = refresher.refreshBearerTokens(
                params = client.refreshTokensParams(),
            )

            assertEquals("updated-access-token", result?.accessToken)
            assertEquals("updated-refresh-token", result?.refreshToken)
            assertEquals(0, tokenStore.saveCount)
            assertEquals(0, tokenStore.clearCount)
            assertEquals(0, notifier.notifyCount)
        }

    private fun createClient(
        content: String,
        status: HttpStatusCode,
        failOnReissue: Boolean = false,
    ): HttpClient =
        HttpClient(
            MockEngine { request ->
                if (request.url.encodedPath == "/protected") {
                    respond(content = "{}", status = HttpStatusCode.OK, headers = jsonHeaders)
                } else if (failOnReissue) {
                    fail("재발급 요청이 호출되지 않아야 합니다.")
                } else {
                    respond(content = content, status = status, headers = jsonHeaders)
                }
            },
        ) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(json)
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

    private suspend fun HttpClient.refreshTokensParams(): RefreshTokensParams =
        RefreshTokensParams(
            client = this,
            response = get("https://prezel.test/protected"),
            oldTokens = BearerTokens(
                accessToken = initialToken.accessToken,
                refreshToken = initialToken.refreshToken,
            ),
        )

    private class FakeAuthTokenStore(
        var token: AuthToken? = initialToken,
        private val clearResult: Result<Unit> = Result.success(Unit),
    ) : AuthTokenStore {
        var saveCount = 0
            private set
        var clearCount = 0
            private set

        override fun getToken(): Flow<AuthToken?> = flowOf(token)

        override suspend fun saveToken(token: AuthToken): Result<Unit> {
            saveCount += 1
            this.token = token
            return Result.success(Unit)
        }

        override suspend fun clear(): Result<Unit> {
            clearCount += 1
            clearResult.onSuccess {
                token = null
            }
            return clearResult
        }
    }

    private class FakeAuthSessionExpiredNotifier : AuthSessionExpiredNotifier {
        var notifyCount = 0
            private set

        override fun notifySessionExpired() {
            notifyCount += 1
        }
    }

    private companion object {
        val initialToken = AuthToken(
            accessToken = "old-access-token",
            refreshToken = "old-refresh-token",
        )
        val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}
