plugins {
    alias(libs.plugins.prezel.android.library.compose)
}

android {
    namespace = "com.team.prezel.core.designsystem"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
}
