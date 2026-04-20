plugins {
    alias(libs.plugins.prezel.android.library.compose)
}

android {
    namespace = "com.team.prezel.core.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.timber)
}
