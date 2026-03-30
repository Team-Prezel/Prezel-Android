import com.android.build.api.dsl.VariantDimension
import com.team.prezel.buildlogic.convention.external.localProperty

plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.auth"

    buildTypes {
        debug {
            setKakaoNativeAppKey("debug")
        }

        release {
            setKakaoNativeAppKey("release")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

private fun VariantDimension.setKakaoNativeAppKey(buildType: String) {
    val kakaoNativeAppKey = localProperty("$buildType.kakao.native.app.key").get()
    buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
    manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kakao.user)
    implementation(libs.timber)
}
