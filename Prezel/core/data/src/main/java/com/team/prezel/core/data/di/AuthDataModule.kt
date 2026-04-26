package com.team.prezel.core.data.di

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
internal abstract class AuthDataModule {
    @Singleton
    @Binds
    abstract fun bindAuthTokenStore(authTokenStore: DataAuthTokenStore): AuthTokenStore

    @Singleton
    @Binds
    abstract fun bindAuthSessionExpiredNotifier(notifier: DataAuthSessionExpiredNotifier): AuthSessionExpiredNotifier

    @Singleton
    @Binds
    abstract fun bindAuthSessionEventPublisher(eventBus: DefaultAuthSessionEventBus): AuthSessionEventPublisher

    @Singleton
    @Binds
    abstract fun bindAuthSessionEventStream(eventBus: DefaultAuthSessionEventBus): AuthSessionEventStream
}
