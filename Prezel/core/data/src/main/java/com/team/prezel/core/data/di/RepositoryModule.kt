package com.team.prezel.core.data.di

import com.team.prezel.core.data.auth.KakaoLoginManager
import com.team.prezel.core.data.auth.KakaoLoginManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    internal abstract fun bindsKakaoLoginManager(kakaoLoginManager: KakaoLoginManagerImpl): KakaoLoginManager
}
