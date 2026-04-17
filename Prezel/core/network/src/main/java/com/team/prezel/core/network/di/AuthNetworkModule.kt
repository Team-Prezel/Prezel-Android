package com.team.prezel.core.network.di

import com.team.prezel.core.network.service.AuthService
import com.team.prezel.core.network.service.createAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AuthNetworkModule {
    @Provides
    @Singleton
    fun provideAuthService(ktorfit: Ktorfit): AuthService = ktorfit.createAuthService()
}
