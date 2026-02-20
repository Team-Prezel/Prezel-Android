plugins {
    alias(libs.plugins.prezel.android.application.compose)
    alias(libs.plugins.prezel.hilt)
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
    implementation(projects.coreUi)
    implementation(projects.featureHomeApi)
    implementation(projects.featureHomeImpl)
    implementation(projects.featureHistoryApi)
    implementation(projects.featureHistoryImpl)
    implementation(projects.featureProfileApi)
    implementation(projects.featureProfileImpl)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
}
