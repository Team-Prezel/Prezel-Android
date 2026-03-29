package com.team.prezel.core.auth.di

import com.team.prezel.core.auth.KakaoLoginManager
import com.team.prezel.core.auth.KakaoLoginManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Singleton
    @Binds
    internal abstract fun bindsKakaoLoginManager(kakaoLoginManager: KakaoLoginManagerImpl): KakaoLoginManager
}
