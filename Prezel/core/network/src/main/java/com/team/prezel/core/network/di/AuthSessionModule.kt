package com.team.prezel.core.network.di

import com.team.prezel.core.network.auth.AuthSessionCache
import com.team.prezel.core.network.auth.AuthSessionCacheImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthSessionModule {
    @Binds
    @Singleton
    abstract fun bindAuthSessionCache(impl: AuthSessionCacheImpl): AuthSessionCache
}
