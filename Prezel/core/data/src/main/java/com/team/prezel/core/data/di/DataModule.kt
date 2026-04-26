package com.team.prezel.core.data.di

import com.team.prezel.core.data.ConnectivityManagerNetworkMonitor
import com.team.prezel.core.data.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun bindsNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor
}
