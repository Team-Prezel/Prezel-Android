import com.team.prezel.buildlogic.convention.external.localProperty

plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.team.prezel.core.auth"

    defaultConfig {
        val kakaoNativeAppKey = localProperty("kakao.native.app.key")

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kakao.user)
    implementation(libs.timber)
}
