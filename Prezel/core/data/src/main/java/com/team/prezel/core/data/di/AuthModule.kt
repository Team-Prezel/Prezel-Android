package com.team.prezel.core.data.di

import com.team.prezel.core.data.auth.DefaultTokenProvider
import com.team.prezel.core.network.auth.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindsTokenProvider(tokenProvider: DefaultTokenProvider): TokenProvider
}
