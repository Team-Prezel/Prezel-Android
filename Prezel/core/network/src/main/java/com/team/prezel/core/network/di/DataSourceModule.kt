package com.team.prezel.core.network.di

import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.datasource.AuthRemoteDataSourceImpl
import com.team.prezel.core.network.datasource.TermsRemoteDataSource
import com.team.prezel.core.network.datasource.TermsRemoteDataSourceImpl
import com.team.prezel.core.network.datasource.UserRemoteDataSource
import com.team.prezel.core.network.datasource.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTermsRemoteDataSource(impl: TermsRemoteDataSourceImpl): TermsRemoteDataSource
}
