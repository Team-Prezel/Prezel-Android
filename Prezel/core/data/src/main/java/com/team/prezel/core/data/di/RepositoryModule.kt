package com.team.prezel.core.data.di

import com.team.prezel.core.data.repository.DefaultAuthRepository
import com.team.prezel.core.data.repository.UserRepositoryImpl
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.repository.profile.UserRepository
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
    abstract fun bindAuthRepository(impl: DefaultAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
