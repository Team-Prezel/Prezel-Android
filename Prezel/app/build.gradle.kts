plugins {
    alias(libs.plugins.prezel.android.application.compose)
    alias(libs.plugins.prezel.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

private val kakaoNativeAppKey = project.findProperty("KAKAO_NATIVE_APP_KEY") as String? ?: ""

android {
    namespace = "com.team.prezel"

    buildTypes {
        debug {
            isMinifyEnabled = false
            resValue("string", "app_name", "Prezel (Dev)")
            applicationIdSuffix = ".dev"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }
}

dependencies {
    implementation(projects.coreData)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreNavigation)
    implementation(projects.coreUi)

    implementation(projects.featureSplashApi)
    implementation(projects.featureSplashImpl)
    implementation(projects.featureLoginApi)
    implementation(projects.featureLoginImpl)
    implementation(projects.featureHomeApi)
    implementation(projects.featureHomeImpl)
    implementation(projects.featureHistoryApi)
    implementation(projects.featureHistoryImpl)
    implementation(projects.featureProfileApi)
    implementation(projects.featureProfileImpl)
    implementation(projects.featureLoginApi)
    implementation(projects.featureLoginImpl)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kakao.user)
}
