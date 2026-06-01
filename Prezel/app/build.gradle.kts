import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.prezel.android.application.compose)
    alias(libs.plugins.prezel.hilt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.google.gms.google.services)
}

val androidVersionCodeProvider = providers
    .gradleProperty("ANDROID_VERSION_CODE")
    .orElse(providers.environmentVariable("ANDROID_VERSION_CODE"))
    .orElse("1")
val androidVersionNameProvider = providers
    .gradleProperty("ANDROID_VERSION_NAME")
    .orElse(providers.environmentVariable("ANDROID_VERSION_NAME"))
    .orElse("0.1.0")
val appApplicationIdProvider = providers
    .gradleProperty("APP_APPLICATION_ID")
    .orElse(providers.environmentVariable("APP_APPLICATION_ID"))
    .orElse("com.team.prezel")
val appNameProvider = providers
    .gradleProperty("APP_NAME")
    .orElse(providers.environmentVariable("APP_NAME"))
    .orElse("Prezel")

android {
    namespace = "com.team.prezel"

    defaultConfig {
        applicationId = appApplicationIdProvider.get()
        versionCode = androidVersionCodeProvider.get().toInt()
        versionName = androidVersionNameProvider.get()
    }

    signingConfigs {
        create("release") {
            val localProperties = gradleLocalProperties(rootDir, providers)

            storeFile = rootProject.file(localProperties.getProperty("signed.store.file"))
            storePassword = localProperties.getProperty("signed.store.password")
            keyAlias = localProperties.getProperty("signed.key.alias")
            keyPassword = localProperties.getProperty("signed.key.password")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            resValue("string", "app_name", "Prezel (Dev)")
            applicationIdSuffix = ".dev"
        }

        release {
            isMinifyEnabled = false
            isShrinkResources = false
            resValue("string", "app_name", appNameProvider.get())
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.coreAuth)
    implementation(projects.coreData)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreDomain)
    implementation(projects.coreNavigation)
    implementation(projects.coreUi)
    implementation(projects.coreCommon)

    implementation(projects.featureSplashApi)
    implementation(projects.featureSplashImpl)
    implementation(projects.featureLoginApi)
    implementation(projects.featureLoginImpl)
    implementation(projects.featureHomeApi)
    implementation(projects.featureHomeImpl)
    implementation(projects.featureHistoryApi)
    implementation(projects.featureHistoryImpl)
    implementation(projects.featureMyApi)
    implementation(projects.featureMyImpl)
    implementation(projects.featureFeedbackApi)
    implementation(projects.featureFeedbackImpl)

    implementation(projects.featureTermsImpl)
    implementation(projects.featurePracticeImpl)
    implementation(projects.featureAnalysisImpl)
    implementation(projects.featureSettingImpl)
    implementation(projects.featureProfileImpl)
    implementation(projects.featureReportImpl)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.orhanobut.logger)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
}
