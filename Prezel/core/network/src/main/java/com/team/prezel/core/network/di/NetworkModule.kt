package com.team.prezel.core.network.di

import com.team.prezel.core.network.client.HttpClientFactory
import com.team.prezel.core.network.service.AuthService
import com.team.prezel.core.network.service.PresentationService
import com.team.prezel.core.network.service.PracticeService
import com.team.prezel.core.network.service.TermsService
import com.team.prezel.core.network.service.UserService
import com.team.prezel.core.network.service.createAuthService
import com.team.prezel.core.network.service.createPresentationService
import com.team.prezel.core.network.service.createPracticeService
import com.team.prezel.core.network.service.createTermsService
import com.team.prezel.core.network.service.createUserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    internal fun provideHttpClient(factory: HttpClientFactory): HttpClient = factory.create()

    @Provides
    @Singleton
    fun provideKtorfit(httpClient: HttpClient): Ktorfit =
        Ktorfit
            .Builder()
            .httpClient(httpClient)
            .build()

    @Provides
    @Singleton
    internal fun provideAuthService(ktorfit: Ktorfit): AuthService = ktorfit.createAuthService()

    @Provides
    @Singleton
    internal fun provideUserService(ktorfit: Ktorfit): UserService = ktorfit.createUserService()

    @Provides
    @Singleton
    internal fun providePracticeService(ktorfit: Ktorfit): PracticeService = ktorfit.createPracticeService()

    @Provides
    @Singleton
    internal fun providePresentationService(ktorfit: Ktorfit): PresentationService = ktorfit.createPresentationService()

    @Provides
    @Singleton
    internal fun provideTermsService(ktorfit: Ktorfit): TermsService = ktorfit.createTermsService()
}
