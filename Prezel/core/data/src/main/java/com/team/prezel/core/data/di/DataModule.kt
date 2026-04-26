package com.team.prezel.core.data.di

import com.team.prezel.core.data.ConnectivityManagerNetworkMonitor
import com.team.prezel.core.data.NetworkMonitor
import com.team.prezel.core.data.auth.DataAuthSessionExpiredNotifier
import com.team.prezel.core.data.auth.DataAuthTokenStore
import com.team.prezel.core.data.auth.DefaultAuthSessionEventBus
import com.team.prezel.core.domain.session.AuthSessionEventPublisher
import com.team.prezel.core.domain.session.AuthSessionEventStream
import com.team.prezel.core.network.auth.AuthSessionExpiredNotifier
import com.team.prezel.core.network.auth.AuthTokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun bindsNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor

    @Singleton
    @Binds
    internal abstract fun bindAuthTokenStore(authTokenStore: DataAuthTokenStore): AuthTokenStore

    @Singleton
    @Binds
    internal abstract fun bindAuthSessionExpiredNotifier(notifier: DataAuthSessionExpiredNotifier): AuthSessionExpiredNotifier

    @Singleton
    @Binds
    internal abstract fun bindAuthSessionEventPublisher(eventBus: DefaultAuthSessionEventBus): AuthSessionEventPublisher

    @Singleton
    @Binds
    internal abstract fun bindAuthSessionEventStream(eventBus: DefaultAuthSessionEventBus): AuthSessionEventStream
}
