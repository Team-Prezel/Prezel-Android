package com.team.prezel.core.auth.di

import com.team.prezel.core.auth.AuthClient
import com.team.prezel.core.auth.KakaoAuthClient
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthProviderKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @IntoMap
    @AuthProviderKey(AuthProvider.KAKAO)
    abstract fun bindKakaoAuthClient(kakaoAuthClient: KakaoAuthClient): AuthClient
}
