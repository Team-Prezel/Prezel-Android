package com.team.prezel.core.network.di

import com.team.prezel.core.datastore.auth.AuthTokenCacheInvalidator
import com.team.prezel.core.network.auth.KtorAuthTokenCacheInvalidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthTokenCacheModule {
    @Binds
    abstract fun bindAuthTokenCacheInvalidator(impl: KtorAuthTokenCacheInvalidator): AuthTokenCacheInvalidator
}
