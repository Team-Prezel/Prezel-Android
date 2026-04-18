package com.team.prezel.core.network.di

import com.team.prezel.core.network.auth.AuthTokenStore
import com.team.prezel.core.network.auth.DataStoreAuthTokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TokenStoreModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenStore(impl: DataStoreAuthTokenStore): AuthTokenStore
}
