package com.team.prezel.core.data.di

import com.team.prezel.core.data.repository.AuthRepositoryImpl
import com.team.prezel.core.data.repository.PracticeRepositoryImpl
import com.team.prezel.core.data.repository.PresentationRepositoryImpl
import com.team.prezel.core.data.repository.TermsRepositoryImpl
import com.team.prezel.core.data.repository.UserRepositoryImpl
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.domain.repository.terms.TermsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindPracticeRepository(impl: PracticeRepositoryImpl): PracticeRepository

    @Binds
    @Singleton
    abstract fun bindPresentationRepository(impl: PresentationRepositoryImpl): PresentationRepository

    @Binds
    @Singleton
    abstract fun bindTermsRepository(impl: TermsRepositoryImpl): TermsRepository
}
