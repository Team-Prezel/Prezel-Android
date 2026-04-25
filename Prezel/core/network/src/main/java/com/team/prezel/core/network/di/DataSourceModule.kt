package com.team.prezel.core.network.di

import com.team.prezel.core.network.datasource.AuthLocalDataSource
import com.team.prezel.core.network.datasource.AuthLocalDataSourceImpl
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.datasource.AuthRemoteDataSourceImpl
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
    abstract fun bindAuthLocalDataSource(impl: AuthLocalDataSourceImpl): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}
