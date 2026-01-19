plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.core.network)

    implementation(libs.kotlinx.coroutines.core)
}
