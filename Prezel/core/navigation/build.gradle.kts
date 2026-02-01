plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.team.prezel.core.navigation"
}

dependencies {
    implementation(libs.androidx.savedstate.compose)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
}
