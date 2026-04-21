plugins {
    alias(libs.plugins.prezel.android.application.compose)
    alias(libs.plugins.prezel.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

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
}

dependencies {
    implementation(projects.coreAuth)
    implementation(projects.coreData)
    implementation(projects.coreDatastore)
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
    implementation(projects.featureMyApi)
    implementation(projects.featureMyImpl)
    implementation(projects.featureProfileImpl)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
}
