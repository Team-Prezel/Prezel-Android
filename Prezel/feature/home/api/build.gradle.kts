plugins {
    alias(libs.plugins.prezel.android.feature.api)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.team.prezel.feature.home.api"
}

dependencies {
    api(projects.coreNavigation)
    implementation(libs.androidx.navigation3.runtime)
}
