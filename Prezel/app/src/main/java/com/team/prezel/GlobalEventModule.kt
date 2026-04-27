package com.team.prezel

import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.common.event.GlobalEventBusImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GlobalEventModule {
    @Binds
    @Singleton
    abstract fun bindsGlobalEventBus(globalEventBus: GlobalEventBusImpl): GlobalEventBus
}
