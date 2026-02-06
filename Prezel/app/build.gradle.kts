plugins {
    alias(libs.plugins.prezel.android.application.compose)
    alias(libs.plugins.prezel.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.team.prezel"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.coreData)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreNavigation)
    implementation(projects.featureHomeApi)
    implementation(projects.featureHomeImpl)
    implementation(projects.featureHistoryApi)
    implementation(projects.featureHistoryImpl)
    implementation(projects.featureProfileApi)
    implementation(projects.featureProfileImpl)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
}
