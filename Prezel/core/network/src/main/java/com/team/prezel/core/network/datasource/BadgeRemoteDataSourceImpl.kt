package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.BuildConfig
import com.team.prezel.core.network.model.badge.BadgeEventResponse
import com.team.prezel.core.network.model.badge.GetBadgeDetailResponse
import com.team.prezel.core.network.model.badge.GetBadgeResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.service.BadgeService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.serverSentEvents
import io.ktor.client.plugins.timeout
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal class BadgeRemoteDataSourceImpl @Inject constructor(
    private val badgeService: BadgeService,
    private val httpClient: HttpClient,
) : BadgeRemoteDataSource {
    override suspend fun getBadges(sort: String): List<GetBadgeResponse> = badgeService.getBadges(sort = sort).requireData()

    override suspend fun getBadgeDetail(badgeCode: String): GetBadgeDetailResponse = badgeService.getBadgeDetail(badgeCode = badgeCode).requireData()

    override fun connectBadgeEventStream(): Flow<BadgeEventResponse> =
        callbackFlow {
            val streamJob = createStreamJob()

            awaitClose {
                streamJob.cancel()
            }
        }

    private fun ProducerScope<BadgeEventResponse>.createStreamJob(): Job =
        launch {
            while (isActive) {
                try {
                    httpClient.serverSentEvents(
                        urlString = badgeStreamUrl,
                        reconnectionTime = BADGE_SSE_RETRY_DELAY_MILLIS.milliseconds,
                        request = {
                            accept(ContentType.Text.EventStream)
                            timeout { socketTimeoutMillis = BADGE_SSE_SOCKET_TIMEOUT_MILLIS }
                        },
                    ) {
                        incoming.collect { event ->
                            BadgeSseEventParser
                                .parse(
                                    eventName = event.event,
                                    data = event.data,
                                )?.let { badgeEvent -> trySend(badgeEvent) }
                        }
                    }
                    close()
                } catch (throwable: CancellationException) {
                    throw throwable
                } catch (throwable: Throwable) {
                    Timber.e(throwable)
                    delay(BADGE_SSE_RETRY_DELAY_MILLIS)
                }
            }
        }

    private companion object {
        private const val BADGE_SSE_SOCKET_TIMEOUT_MILLIS = 90_000L
        private const val BADGE_SSE_RETRY_DELAY_MILLIS = 1_000L
        private val badgeStreamUrl = "${BuildConfig.BASE_URL.trimEnd('/')}/api/stream/badges"
    }
}
