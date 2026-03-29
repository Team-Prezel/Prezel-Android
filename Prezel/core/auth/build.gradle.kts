import com.team.prezel.buildlogic.convention.external.localProperty

plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.auth"

    defaultConfig {
        val kakaoNativeAppKey =
            localProperty("kakao.native.app.key").orNull
                ?: error("kakao.native.app.key가 local.properties에 없습니다.")

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kakao.user)
    implementation(libs.timber)
}
