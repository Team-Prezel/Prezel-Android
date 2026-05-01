package com.team.prezel.core.auth.di

import com.team.prezel.core.auth.AuthClient
import com.team.prezel.core.auth.KakaoAuthClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindKakaoAuthClient(kakaoAuthClient: KakaoAuthClient): AuthClient
}
