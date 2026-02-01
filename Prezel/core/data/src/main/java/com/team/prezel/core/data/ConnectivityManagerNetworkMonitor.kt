package com.team.prezel.core.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import androidx.core.content.getSystemService
import androidx.core.os.trace
import com.team.prezel.core.network.Dispatcher
import com.team.prezel.core.network.PrezelDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class ConnectivityManagerNetworkMonitor
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
        @param:Dispatcher(PrezelDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    ) : NetworkMonitor {
        override val isOnline: Flow<Boolean> = callbackFlow {
            trace("NetworkMonitor.callbackFlow") {
                val connectivityManager = context.getSystemService<ConnectivityManager>()
                if (connectivityManager == null) {
                    channel.trySend(false)
                    channel.close()
                    return@callbackFlow
                }

                /**
                 * 이 콜백의 메서드들은 활성 네트워크(active network)에 한정되지 않고, NetworkRequest 조건을 만족하는 모든 네트워크의 변경 사항에 대해 호출됩니다.
                 * 따라서 이러한 Network가 존재하는지 여부만 추적하면 됩니다.
                 */
                val callback = object : NetworkCallback() {
                    private val networks = mutableSetOf<Network>()

                    override fun onAvailable(network: Network) {
                        networks += network
                        channel.trySend(true)
                    }

                    override fun onLost(network: Network) {
                        networks -= network
                        channel.trySend(networks.isNotEmpty())
                    }
                }

                trace("NetworkMonitor.registerNetworkCallback") {
                    val request = Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build()
                    connectivityManager.registerNetworkCallback(request, callback)
                }

                /**
                 * 현재 네트워크 연결 상태를 하위 채널로 즉시 전달합니다.
                 */
                channel.trySend(connectivityManager.isCurrentlyConnected())

                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }
        }.flowOn(ioDispatcher)
            .conflate()

        private fun ConnectivityManager.isCurrentlyConnected(): Boolean {
            val networkCapabilities = getNetworkCapabilities(activeNetwork) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
