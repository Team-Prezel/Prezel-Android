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
    implementation(projects.featureHomeApi)
    implementation(projects.featureHomeImpl)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.timber)
    implementation(libs.androidx.navigation3.runtime)
}
