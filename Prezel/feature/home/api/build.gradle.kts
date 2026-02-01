plugins {
    alias(libs.plugins.prezel.android.feature.api)
}

android {
    namespace = "com.team.prezel.feature.home.api"
}

dependencies {
    api(projects.coreNavigation)
    implementation(libs.androidx.navigation3.runtime)
}
