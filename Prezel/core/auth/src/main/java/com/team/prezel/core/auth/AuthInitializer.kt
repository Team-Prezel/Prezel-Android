package com.team.prezel.core.auth

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.qualifiers.ApplicationContext

object AuthInitializer {
    fun init(
        @ApplicationContext context: Context,
    ) {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
