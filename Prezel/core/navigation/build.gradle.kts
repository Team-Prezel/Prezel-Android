plugins {
    alias(libs.plugins.prezel.android.library.compose)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.navigation"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.kotlinx.collections.immutable)
}
