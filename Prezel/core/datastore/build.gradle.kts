plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.datastore"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.coreCommon)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
}
