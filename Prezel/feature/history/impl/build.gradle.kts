plugins {
    alias(libs.plugins.prezel.android.feature.impl)
    alias(libs.plugins.prezel.android.library.compose)
}

android {
    namespace = "com.team.prezel.feature.history.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.featureHistoryApi)
}
