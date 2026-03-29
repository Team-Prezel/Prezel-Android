package com.team.prezel.core.auth

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.team.prezel.core.auth.BuildConfig

object KakaoAuthInitializer {
    fun init(context: Context) {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
