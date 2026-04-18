package com.team.prezel.core.datastore.di

import com.team.prezel.core.datastore.auth.AuthTokenStore
import com.team.prezel.core.datastore.auth.DataStoreAuthTokenStore
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
